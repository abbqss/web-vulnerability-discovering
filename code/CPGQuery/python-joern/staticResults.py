'''
@author:: Abeer Alhuzali
'''

import StringIO


class staticResults(object):
    """
    Class to save and analyze the output of the static analysis component of NAVEX.
    """

    def __init__(self, file="", line_no=-1, node_id=-1, formulas="", query_time=-1):
        '''
        Constructor
        '''
        self.file = file
        self.line_no = line_no
        self.node_id = node_id
        self.query_time = query_time
        self.formulas = formulas

    def stripDataFromOutput(self, output):
        """
        Extract the data from the static analysis output.
        Example output for a found vulnerbility:
        
        {u'[Vulnerable sink formula: file: /var/www/html/mybloggie/adduser.php, 
          line:108, node id: 8488]': [[u'left: $result, right: $temp_8491, op: AST_ASSIGN, type: AST_ASSIGN, node_id: 8488', 
       
        AND for safe sinks : 
           Not A vulnerable sink: file: /var/www/html/mybloggie/includes/function.php, line:39, node id: 24032

        """
        buf = StringIO.StringIO(output)

        # Extract only the vulnerable sinks info 
        start_index = len("{u'[Vulnerable sink formula: file: ")
        end_index = len("]': ")
        allLine = buf.readline()

        filepath, line_no, node_id, rest = allLine[start_index:end_index].split(", ")
        line_no = line_no.split("line: ")
        node_id = node_id.split("node_id: ")

        # now we have to extract the formulas

        self.file = filepath.strip()
        self.line_no = int(line_no)
        self.node_id = int(node_id)

    def setQueryFile(self, file):
        self.file = file

    def setQueryTime(self, qt):
        self.query_time = qt

    def getQueryTime(self):
        if self.query_time > 0:
            return self.query_time

        else:
            raise Exception(
                "Query time value is wrong or not set "
                " Query time: %d" % (self.query_time)
            )
