import sys

from Analysis import Analysis

"""
  Author: Abeer Alhuzali
  This program runs the traversal that identifies vulnerable sinks 
  AND creates an inclusion map of the apps..
  For more information, please read "NAVEX: Precise and Scalable Exploit Generation for Dynamic Web Applications"
  
"""


def main(argv):
    if len(argv) == 2:
        attackType = argv[1].strip()
    else:
        attackType = "sql"
    sa = Analysis(7474)
    query = sa.prepareQueryStatic(attackType)
    print('the query is ')
    print(query)
    result, elapsed_time = sa.runTimedQuery(query)
    print (result)
    # writeToFile(result, elapsed_time, attackType)
    #
    # result, elapsed_time =sa.runTimedQuery("g.V().includeMap()")
    # writeIncludeMapToFile(result, elapsed_time)


# edit paths to result files as needed
def writeToFile(result, elapsed_time, attackType):
    if attackType == "sql":
        f = open('/home/user/navex/results/static_analysis_results.txt', 'w')
    elif attackType == "xss":
        f = open('/home/user/navex/results/static_analysis_results_xss.txt', 'w')
    elif attackType == "code":
        f = open('/home/user/navex/results/static_analysis_results_code.txt', 'w')
    elif attackType == "os-command":
        f = open('/home/user/navex/results/static_analysis_results_os-command.txt', 'w')
    elif attackType == "file-inc":
        f = open('/home/user/navex/results/static_analysis_results_file-inc.txt', 'w')
    elif attackType == "ear":
        f = open('/home/user/navex/results/static_analysis_results_ear.txt', 'w')

    for node in result:
        print (node)
        print>> f, node
    print>> f, elapsed_time

    f.close()


# edit paths to result files as needed. 
# Note, you have to change paths in Navex/solver/StaticSolver.java accordingly
def writeIncludeMapToFile(result, elapsed_time):
    f = open('/home/user/navex/results/include_map_results.txt', 'w')
    for node in result:
        print (node)
        print>> f, node
    print>> f, elapsed_time

    f.close()


if __name__ == '__main__':
    main(sys.argv)
