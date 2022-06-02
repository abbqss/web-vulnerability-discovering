/**
 Author Abeer Alhuzali
 The following steps and functions are used to construct TAC formulas from different
 AST nodes.
 */

Gremlin.defineStep('callFormulaAll', [Vertex, Pipe], { node ->
    def varList = [];//, formula = [];
    node
            .astNodes()
            .dedup()
            .filter { it.type == TYPE_CALL }

            .sideEffect { formula = (it.callFormulaNew(it)) }

            .transform { formula }


});

Gremlin.defineStep('callFormulaNew', [Vertex, Pipe], { node ->
    def varList = [];//, formula = [];
    node
            .ithChildren(0)
            .sideEffect { methodName = it }
            .as('astCallName')
            .sideEffect { mname = it.ithChildren(0).next().code }
            .parents()
            .callToArguments() //OK but no recursion


            .resolveCallArgsNew()
            .aggregate(varList)

            .transform { tacFormula(varList.toList(), '$temp_' + node.id, mname, node.type, node.id) } //OK


});

Gremlin.defineStep('resolveCallArgsNew', [Vertex, Pipe], {
    def l = [];
    _().sideEffect { org = it }
            .or(
                    _().has('type', T.eq, 'AST_DIM')
                            .transform {

                                if (it.ithChildren(1).type.next() == 'AST_CONST')
                                    v = it.ithChildren(1).out.out.code.next();

                                else
                                    v = it.ithChildren(1).code.next();
                                l = '$' + it.ithChildren(0).varToName.next() + '[' + v + ']';


                            }

                    ,
                    _().has('type', T.eq, TYPE_VAR)
                            .sideEffect { l = (it.varFormula().next()) }

                    ,

                    _().has('type', T.eq, TYPE_CALL)
                            .sideEffect { l = '$temp_' + it.id }

                    ,
                    _().has('type', T.eq, "string")
                            .ifThenElse { it.code != null }
                                    { it.sideEffect { l = ("\"" + it.code + "\"") } }
                                    { it.sideEffect { l = ('\"\"') } }

                    ,
                    _().has('type', T.eq, "integer")
                            .sideEffect { l = it.code }
            )

            .transform { l }.scatter().dedup()
});


Gremlin.defineStep('resolveArgsNew2', [Vertex, Pipe], {
    def l = [];
    _().sideEffect { org = it }
            .or(
                    _().has('type', T.eq, 'AST_DIM')
                            .transform {

                                if (it.ithChildren(1).type.next() == 'AST_CONST')
                                    v = it.ithChildren(1).out.out.code.next();

                                else
                                    v = it.ithChildren(1).code.next();
                                l = '$' + it.ithChildren(0).varToName.next() + '[' + v + ']';


                            }

                    ,
                    _().has('type', T.eq, TYPE_VAR)
                            .sideEffect { l = (it.varFormula().next()) }

                    ,

                    _().has('type', T.eq, "string")
                            .ifThenElse { it.code != null }
                                    { it.sideEffect { l = ("\"" + it.code + "\"") } } //"+it.code+"
                                    { it.sideEffect { l = ('\"\"') } }

                    ,
                    _().has('type', T.eq, "integer")
                            .sideEffect { l = it.code }
            )

            .transform { l }.scatter().dedup()
});

Gremlin.defineStep('resolveCallArgsNew3', [Vertex, Pipe], {
    def l = [];
    _().sideEffect { org = it }
            .or(
                    _().has('type', T.eq, 'AST_DIM')
                            .transform {

                                if (it.ithChildren(1).type.next() == 'AST_CONST')
                                    v = it.ithChildren(1).out.out.code.next();

                                else
                                    v = it.ithChildren(1).code.next();
                                l = '$' + it.ithChildren(0).varToName.next() + '[' + v + ']';


                            }

                    ,
                    _().has('type', T.eq, TYPE_VAR)
                            .sideEffect { l = (it.varFormula().next()) }

                    ,

                    _().has('type', T.eq, TYPE_BINARY_OP)
                            .sideEffect { l = it.resolveBinaryArgs()}

                    ,

                    _().has('type', T.eq, TYPE_CALL)
                            .sideEffect { l = '__call__' + it.ithChildren(0).out().code.toList()}

                    ,

                    _().has('type', T.eq, "string")
                            .ifThenElse { it.code != null }
                                    { it.sideEffect { l = ("\"" + it.code + "\"") } }
                                    { it.sideEffect { l = ('\"\"') } }

                    ,
                    _().has('type', T.eq, "integer")
                            .sideEffect { l = it.code }
            )

            .transform { l }.scatter().dedup()
});

Gremlin.defineStep('containsLowSource', [Vertex, Pipe], {
    def attacker_sources2 = ["_GET", "_POST", "_COOKIE", "_REQUEST", "_ENV", "HTTP_ENV_VARS", "HTTP_POST_VARS", "HTTP_GET_VARS"]
    _().ifThenElse { isAssignment(it) }
    {
        it
                .as('assign')
                .as('assign')
                .rval()
                .children()
                .loop('assign') { it.object != null } { true }
                .match { it.type == "AST_VAR" }
                .filter { attacker_sources2.contains(it.varToName().next()) }
    }

    {
        it
                .astNodes()
                .match { it.type == 'AST_VAR' }
                .filter { attacker_sources2.contains(it.varToName().next()) }
                .in('PARENT_OF')
    }
            .dedup()


});


def startFormula(List flow, def queryMapList, def sink) {

    def finalList = [];
    def f = [] as ArrayList;


    //add db constraints
    if (queryMapList != []) {
        for (qml in queryMapList) {
            f.add(tacFormula(qml.key, qml.value, 'db', 'db', 0));
        }
    }
    f.add(test_getVarsInQuery(sink, flow))


    for (node in flow) {
        if (node instanceof Vertex) {
            if (node.type == TYPE_CALL || node.type == TYPE_METHOD_CALL || node.type == TYPE_STATIC_CALL)

            {
                f.add(node.callFormulaNew(node))
            } else if (node.type == 'AST_DIM') {
                rr = '$' + node.ithChildren(0).varToName.next() + '[' + node.ithChildren(1).code.next() + ']';
                f.add(tacFormula('', rr, node.type, node.type, node.id));


            } else if (node.type == TYPE_BINARY_OP && (node.flags.contains(FLAG_BINARY_EQUAL)
                    || node.flags.contains(FLAG_BINARY_NOT_EQUAL)
                    || node.flags.contains(FLAG_BINARY_IS_IDENTICAL)
                    || node.flags.contains(FLAG_BINARY_IS_NOT_IDENTICAL))) {
                f.add(node.resolveBinaryArgs())

            } else if (node.type == TYPE_ASSIGN) {
                temp = [];
                Vertex r = node.ithChildren(1).next();

                Vertex l = node.ithChildren(0).next();
                temp = resolveAssignmentHelper2(node, l, r);
                temp = temp.flatten().unique();
                if (temp.size != 0)
                    f.add(temp);

            }
        }
    }

    return f;
}


def resolveAssignmentHelper2(Vertex node, Vertex l, Vertex r) {
    def rr, ll;
    def formula = [];
    def m = [];

    if (l.type == 'AST_VAR')
        ll = '$' + l.varToName().next();
    else if (l.type == 'AST_DIM') {

        if (l.ithChildren(1).type.next() == 'AST_CONST')
            v = l.ithChildren(1).out.out.code.next();

        else
            v = l.ithChildren(1).code.next();
        ll = '$' + l.ithChildren(0).varToName.next() + '[' + v + ']';

    }

    if (r.type == 'AST_VAR') {
        rr = '$' + r.varToName().next();
        formula.add(tacFormula(ll, rr, node.type, node.type, node.id));

    } else if (r.type == 'AST_DIM') {
        if (r.ithChildren(1).type.next() == 'AST_CONST')
            v = r.ithChildren(1).out.out.code.next();

        else
            v = r.ithChildren(1).code.next();

        rr = '$' + r.ithChildren(0).varToName.next() + '[' + v + ']'; //'+r.ithChildren(1).code.next()+'
        formula.add(tacFormula(ll, rr, node.type, node.type, node.id));


    } else if (r.type == TYPE_CALL || r.type == TYPE_METHOD_CALL || r.type == TYPE_STATIC_CALL) {

        temp = "left: " + ll + ", right: " + '$temp_' + r.id + ", op: " + node.type + ", type: " + node.type + ", node_id: " + node.id;
        formula.add(temp);

        x = r.callFormulaAll(r);
        formula.add(x);

    }
    //left = child0 ? child1 : child2
    //the formula would be:
    //left = child1 OR left = child2

    else if (r.type == "AST_CONDITIONAL") {
        x = []
        x = r.ithChildren(1).next().resolveCallArgsNew().toList();
        x = x + (r.ithChildren(2).resolveCallArgsNew());
        temp = "left: " + x + ", right: " + ll + ", op: ?, type: " + node.type + ", node_id: " + node.id;
        formula.add(temp);

        formula.add(resolveAssignmentHelper2(r, r.ithChildren(1).next(),
                r.ithChildren(2).next()));

    } else if (r.type == 'AST_BINARY_OP' && r.flags != null && r.flags.contains("BINARY_CONCAT")) {
        t = r.resolveConcatArgs();
        formula.add(t);
        formula.add(tacFormula(ll, '$temp_' + r.id, node.type, node.type, node.id))

        for (Vertex x : r.astNodes()) {
            if (x.flags != null && x.flags.contains("BINARY_CONCAT")) {
                t = x.resolveConcatArgs();
                formula.add(t);
            }

        }
    } else {
    }
    return formula.flatten().unique();

}


// sinkType e.g sql, xss , etc.
def warning(filename, lineno, id, sinkType, uid) {
    "Vulnerable sink formula: file: " + filename + ", line: " + lineno + ", node_id: " + id + ", sinkType: " + sinkType + ", unique_id: " + uid
//+", formula: " +formula
    //
}

def report(filename, lineno, id) {
    "Not A vulnerable sink: file: " + filename + ", line: " + lineno + ", node_id: " + id//+", formula: " +formula

}




