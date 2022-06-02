//Modified by: Abeer Alhuzali

/**
 Retrieve calls by name.

 @param callee Name of called function


 */

Gremlin.defineStep("getCallsToNew", [Vertex, Pipe], {


    _().in(CALLS_EDGE)
    //.parents()

})


Gremlin.defineStep("getCallsTo", [Vertex, Pipe], { N ->


    _().in(CALLS_EDGE)
    //.parents()

})



