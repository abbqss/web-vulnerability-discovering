'''
@Author: Abeer Alhuzali
'''

import time

from joern.all import JoernSteps


class Analysis(object):
    SQL_QUERY_FUNCS = """sql_query_funcs = [
                "mysql_query", "mysqli_query", "pg_query", "sqlite_query"
                ]\n"""

    XSS_FUNCS = """xss_funcs = [
                "print", "echo"
                ]\n"""

    OS_COMMAND_FUNCS = """os_command_funcs = [
               "backticks", "exec" , "expect_popen","passthru","pcntl_exec",
               "popen","proc_open","shell_exec","system", "mail"     
               ]\n"""

    def __init__(self, port):
        '''
        Constructor
        '''
        self.j = JoernSteps()
        self.j.setGraphDbURL('http://localhost:%d/db/data/' % (int(port)))
        self.j.connectToDatabase()

    def prepareQueryStatic(self, attackType):
        query = self.XSS_FUNCS + self.SQL_QUERY_FUNCS + self.OS_COMMAND_FUNCS

        query += "  m =[]; "
        if attackType == "sql":

            query += """ queryMapList =[]; g.V().filter{sql_query_funcs.contains(it.code)  && isCallExpression(it.nameToCall().next()) }.callexpressions()
                            .sideEffect{m = start(it, [], 0, 'sql', false, queryMapList)}
                            .sideEffect{ warnmessage = warning(it.toFileAbs().next().name, it.lineno, it.id, 'sql', '1')}
                            .sideEffect{ reportmessage = report(it.toFileAbs().next().name, it.lineno, it.id)}
                            .ifThenElse{m.isEmpty()}
                              {it.transform{reportmessage}}
                              {it.transform{findSinkLocation(m, warnmessage, 'sql', queryMapList, it)}}"""
        elif attackType == "xss":
            query += """ queryMapList = []; g.V().filter{it.type == TYPE_ECHO || it.type == TYPE_PRINT}
                            .sideEffect{m = start(it, [], 0, 'xss', false, queryMapList)}
                            .sideEffect{ warnmessage = warning(it.toFileAbs().next().name, it.lineno, it.id, 'xss', '1')}
                            .sideEffect{ reportmessage = report(it.toFileAbs().next().name, it.lineno, it.id)}
                            .ifThenElse{m.isEmpty()}
                              {it.transform{reportmessage}}
                              {it.transform{findSinkLocation(m, warnmessage, 'xss', queryMapList, it)}}"""


        elif attackType == "code":
            query += """queryMapList =[]; g.V().filter{it.type == TYPE_INCLUDE_OR_EVAL && it.flags.contains(FLAG_EXEC_EVAL)}
                            .sideEffect{m = start(it, [], 0, 'code', false, queryMapList )}
                            .sideEffect{ warnmessage = warning(it.toFileAbs().next().name, it.lineno, it.id, 'code', '1')}
                            .sideEffect{ reportmessage = report(it.toFileAbs().next().name, it.lineno, it.id)}
                            .ifThenElse{m.isEmpty()}
                              {it.transform{reportmessage}}
                              {it.transform{findSinkLocation(m, warnmessage, 'code', queryMapList, it)}}"""

        # command execution : sinks considered are :
        # [backticks, exec,expect_popen,passthru,pcntl_exec,popen,proc_open,shell_exec,system,mail]
        elif attackType == "os-command":
            query += """queryMapList =[] g.V().filter{os_command_funcs.contains(it.code)  && isCallExpression(it.nameToCall().next()) }.callexpressions()
                            .filter{os_command_funcs.contains(it.ithChildren(0).out.code.next())}
                            .sideEffect{m = start(it, [], 0, 'os-command', false, queryMapList )}
                            .sideEffect{ warnmessage = warning(it.toFileAbs().next().name, it.lineno, it.id, 'os-command', '1')}
                            .sideEffect{ reportmessage = report(it.toFileAbs().next().name, it.lineno, it.id)}
                            .ifThenElse{m.isEmpty()}
                              {it.transform{reportmessage}}
                              {it.transform{findSinkLocation(m, warnmessage, 'os-command', queryMapList, it)}}"""

        elif attackType == "file-inc":
            query += """queryMapList =[]; g.V().filter{it.type == TYPE_INCLUDE_OR_EVAL && !(it.flags.contains(FLAG_EXEC_EVAL))}
                            .sideEffect{m = start(it, [], 0, 'file-inc', false, queryMapList)}
                            .sideEffect{ warnmessage = warning(it.toFileAbs().next().name, it.lineno, it.id, 'file-inc', '1')}
                            .sideEffect{ reportmessage = report(it.toFileAbs().next().name, it.lineno, it.id)}
                            .ifThenElse{m.isEmpty()}
                              {it.transform{reportmessage}}
                              {it.transform{findSinkLocation(m, warnmessage, 'file-inc', queryMapList, it)}}"""
        elif attackType == "ear":
            query += """ g.V().filter{ "header" == it.code  && isCallExpression(it.nameToCall().next()) }.callexpressions()
                  .ithChildren(1).astNodes()
                 .filter{it.code != null && it.code.startsWith("Location")}
                 .callexpressions()
                 .as('call')
                 .out('FLOWS_TO')
                 .filter{it.type != "AST_EXIT" && it.type != "NULL" }
                 .or(
                        _().filter{it.type == "AST_CALL"}
                           .sideEffect{n = jumpToCallingFunction(it)}
                           .filter{n.type != "AST_EXIT" && n.type != "NULL" && n.type != "AST_RETURN"} 
                    ,
                       _().filter{it.type == "AST_CALL"}
                           .sideEffect{n = jumpToCallingFunction(it)}
                           .filter{n.type == "AST_RETURN"}
                           .out('FLOWS_TO')
                           .filter{n.type != "AST_EXIT" && n.type != "NULL" } 
                    ,
                       _().filter{it.type != "AST_CALL"}
                    
                    , _().as('b')
                 .filter{it.type == "AST_CALL"}
                        
                        .astNodes()
                        .filter{it.code != null &&
                                 it.code != "/home/user/log/codeCoverage.txt"}
                         .back('b')
                    )
                 .back('call')
                 .sideEffect{ warnmessage = warning(it.toFileAbs().next().name, it.lineno, it.id, 'ear', '1')}
                 .transform{warnmessage}"""

        return query

    def prepareFinalQuery(self, seed):
        get = []
        for g in seed.get:
            if '=' in g:
                t = g[0:g.find('=')]
                get.append('?' + t + '=')
                get.append('&' + t + '=')

        params = []
        for p in seed.params:
            if '=' in p:
                params.append(p[0:p.find('=')] + '=')

        query = """g.V('url', '%s')
                .findNavigationSeq(%s, %s, %s).dedup().path""" % (seed.src, seed.dst, get, params)
        print (query)
        # {it.url}

        return query

    def runQuery(self, query):
        return query

    def runTimedQuery(self, query):
        start = time.time()
        res = None
        try:
            if query:
                res = self.j.runGremlinQuery(query)




        except Exception as err:
            print "Caught exception:", type(err), err

        elapsed = time.time() - start

        timestr = "Query done in %f seconds." % (elapsed)

        return (res, timestr)

    def readExploitSeedsFile(self, attackType):
        if attackType == "sql":
            print ('Reading Exploit Seeds File in /home/user/navex/results/include_map_resolution_results_xss.txt')
            file = '/home/user/navex/results/include_map_resolution_results.txt'
        elif attackType == "xss":
            file = '/home/user/navex/results/include_map_resolution_results_xss.txt'
            print ('Reading Exploit Seeds File in /home/user/navex/results/include_map_resolution_results_xss.txt')
        elif attackType == "code":
            file = '/home/user/navex/results/include_map_resolution_results_code.txt'
            print ('Reading Exploit Seeds File in /home/user/navex/results/include_map_resolution_results_code.txt')
        elif attackType == "os-command":
            file = '/home/user/navex/results/include_map_resolution_results_os-command.txt'
            print (
                'Reading Exploit Seeds File in /home/user/navex/results/include_map_resolution_results_os-command.txt')
        elif attackType == "file-inc":
            file = '/home/user/navex/results/include_map_resolution_results_file-inc.txt'
            print ('Reading Exploit Seeds File in /home/user/navex/results/include_map_resolution_results_file-inc.txt')
        elif attackType == "ear":
            file = '/home/user/navex/results/include_map_resolution_results_ear.txt'
            print ('Reading Exploit Seeds File in /home/user/navex/results/include_map_resolution_results_ear.txt')

        with open(file, 'r') as f:
            lines = [line.strip() for line in f]

        return lines
