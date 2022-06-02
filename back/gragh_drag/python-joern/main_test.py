import json
from pprint import pprint

from Analysis import Analysis

attackType = "sql"
sa = Analysis(7474)
query = sa.prepareQueryStatic(attackType)

query = """
sql_query_funcs = ["mysql_query", "mysqli_query", "pg_query", "sqlite_query"]

repairs = ["md5", "addslashes", "mysqli_real_escape_string", "mysql_escape_string"]

Paths = []

VulnerablePaths = g.V().filter{sql_query_funcs.contains(it.code)  && isCallExpression(it.nameToCall().next())}.as('sink')
.callexpressions().as('sloop').statements().inE('REACHES').outV.loop('sloop')
{ it.loops < 10  }{ it.object.containsLowSource().toList() != []}.path().toList()


for ( vpaths in VulnerablePaths){
    vlen = vpaths.size()
    p = []

    for (int i = 1; i < vlen ; i+=2) {
        node = vpaths[i];
        if ( i+1 < vlen){
            source_var = vpaths[i+1].var;
        }
        else{
            source_var = vpaths[i].match {
                        it.type == "AST_VAR" &&
                        it.containsLowSource().toList().size() > 0 }.varToName().toList()[0]
        }

        alive_vars = node.match{isAssignment(it)}.rval().match {
                    it.type == "AST_VAR" &&
                    it.varToName().toList().contains(source_var) &&
                    it.argToCall().transform{getFuncName(it)}.toList().intersect(repairs) == [] 
                    }.varToName().toList()


        if (alive_vars.contains(source_var)){
              p.add(node);
        }else{
            p = []
            i = vlen
        }
    }

    if(p.isEmpty()){
        continue
    }
    else{
        Paths.add(vpaths)
        p =[]
    }

}


result = []
for (x in Paths){
    result.add([x[0].toFileAbs().next().name, x[0].lineno, getFuncName(x[0])])
    for (int i = 1; i < x.size() ; i+=2){
        result.add([x[i].toFileAbs().next().name, x[i].lineno, x[i].match{it.type == 'AST_VAR'}.resolveCallArgsNew3()])
    }
    result.add("------------------")
}

result


"""

result, elapsed_time = sa.runTimedQuery(query)
print(query)
print("result:")
# print (result)
# exit()
if type(result) == list:
    print("len:", len(result))
    for x in result:
        # print(type(x), type("AST_CALL"))
        print(x)
    exit()
try:
    print (json.dumps(result, indent=2))
except:
    print (result)
