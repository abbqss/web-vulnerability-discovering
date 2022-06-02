//Author Abeer Alhuzali

/**
 This step is used to find the concrete exploits (as explained in Navex paper)
 */

Gremlin.defineStep('findNavigationSeq', [Vertex, Pipe], { dst, get, params ->
    def x = [], y = []
    _()
            .as('down')
            .dynamicChildren()
            .sideEffect { foundDst = containsInList(dst, it.url) }
            .sideEffect { foundGet = containsInList(get, it.get) }
            .sideEffect { foundParam = containsInList(params, it.params) }

            .loop('down') { it.loops < 5 && (!foundDst || !foundParam || !foundGet) }
            .filter { containsInList(get, it.url) }
            .filter { containsInList(params, it.params) }
            .aggregate(x)


})

boolean containsInList(List list, String toFind) {
    if (list.size() == 0)
        return true;
    if (toFind == null)
        return false;
    for (String l in list) {
        if (toFind.contains(l))
            return true;
    }
    return false;
}


/**
 Traverse to parent-nodes of the navigation graph nodes
 (constructed by a dynamic analysis).
 */

Gremlin.defineStep('dynamicParents', [Vertex, Pipe], {
    _().in()
})

/**
 Traverse to child-nodes of the navigation graph nodes.
 */

Gremlin.defineStep('dynamicChildren', [Vertex, Pipe], {
    _().out()
})

