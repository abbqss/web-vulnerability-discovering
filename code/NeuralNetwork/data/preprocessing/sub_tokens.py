
from phply import phplex
sql = [
	# // Abstraction Layers
	# // 'dba_open',
	# // 'dba_popen',
	'dba_insert',
	'dba_fetch',
	'dba_delete',
	'dbx_query',
	'odbc_do',
	'odbc_exec',
	'odbc_execute',
	'db2_exec' ,
	'db2_execute',
	'fbsql_db_query',
	'fbsql_query',
	'ibase_query',
	'ibase_execute',
	'ifx_query',
	'ifx_do',
	'ingres_query',
	'ingres_execute',
	'ingres_unbuffered_query',
	'msql_db_query',
	'msql_query',
	# // 'msql',
	'mssql_query',
	'mssql_execute',
	'mysql_connect',
	'mysql_select_db',
	'mysql_close',
	'mysql_db_query',
	'mysql_query',
	'mysql_fetch_array',
	'mysql_unbuffered_query',
	'mysqli_stmt_execute',
	'mysqli_query',
	'mysqli_real_query',
	'mysqli_master_query',
	'oci_execute',
	'ociexecute',
	'ovrimos_exec',
	'ovrimos_execute',
	'ora_do',
	'ora_exec',
	'pg_query',
	'pg_send_query',
	'pg_send_query_params',
	'pg_send_prepare',
	'pg_prepare',
	'sqlite_open',
	'sqlite_popen',
	'sqlite_array_query',
	'arrayQuery',
	'singleQuery',
	'sqlite_query',
	'sqlite_exec',
	'sqlite_single_query',
	'sqlite_unbuffered_query',
	'sybase_query',
	'sybase_unbuffered_query',


]

exec = [
		'backticks',
		'exec',
		'expect_popen',
		'passthru',
		'pcntl_exec',
		'popen',
		'proc_open',
		'shell_exec',
		'system',
		'eval',
		]

preg = [
		'preg_filter',
		'preg_grep',
		'preg_last_error',
		'preg_match_all',
		'preg_match',
		'preg_quote',
		'preg_replace_callback',
		'preg_replace',
		'ereg_replace',
		'ereg',
		'eregi_replace',
		'eregi',
	]

input = [
		'$_GET',
		'$_POST',
		'$_COOKIE',
		'$_REQUEST',
		'$_FILES',
		'$_SERVER',
		'$HTTP_GET_VARS',
		'$HTTP_POST_VARS',
		'$HTTP_COOKIE_VARS',
		'$HTTP_REQUEST_VARS',
		'$HTTP_POST_FILES',
		'$HTTP_SERVER_VARS',
		'$HTTP_RAW_POST_DATA',
		'$argc',
		'$argv',
		'get_headers',
		'getallheaders',
		'get_browser',
		'import_request_variables',
	]

echo = [
	'echo',
	'print_r',
	'printf',
	'vprintf',
	'trigger_error',
	'user_error',
	'odbc_result_all',
	'ifx_htmltbl_result'
]

html = [
	'htmlspecialchars',
	'htmlentities',
	'ENT_QUOTES',
]

filtering = [
	'filter_var',
	'mysql_real_escape_string',
	'addslashes',
]

additionals = [
	'proc_open',
	'proc_close',
	'rawurlencode',
	'is_resource',
	'fgets',
	'in_array',
	'sprintf',
	'unserialize',
	'fread',
	'fopen',
	'fclose',
	'urlencode',
	'escapeshellarg',
	'http_build_query',
	'FILTER_SANITIZE_EMAIL',
	'FILTER_VALIDATE_EMAIL',
	'FILTER_SANITIZE_FULL_SPECIAL_CHARS',
	'FILTER_SANITIZE_MAGIC_QUOTES',
	'FILTER_SANITIZE_NUMBER_FLOAT',
	'FILTER_VALIDATE_FLOAT',
	'FILTER_SANITIZE_NUMBER_INT',
	'FILTER_VALIDATE_INT',
	'FILTER_SANITIZE_SPECIAL_CHARS',
]

casting = [
	# 'float',
	# 'int',
	'settype',
	'floatval',
	'intval',
]

def RepresentsInt(s):
    try:
        int(s)
        return True
    except ValueError:
        return False



#sql stuff
# 'sprintf', 'unserialize', 'preg_match', 'pclose', 'fread', 'proc_open', 'fopen', 'exec', 'popen', 'fclose', 'stream_get_contents', 'proc_close', 'fgets', 'shell_exec', 'mysql_real_escape_string', 'filter_var', 'system', 'is_resource'

combined = sql + echo + input + preg +  exec + filtering + html + additionals + casting + ["FUNCTION_CALL"]
def sub_token(token, allfunc=None,allvar=None):
	if token.value in combined :
		return token.value.upper()
	else:
		if allfunc is not None and token.value in allfunc:
			return 'FUNCTION_CALL'
		if allvar is not None and token.type == 'VARIABLE' :
			if token.value not in allvar:
				allvar.append(token.value)
			if allvar.index(token.value) < 10:
				return 'VAR' + str(allvar.index(token.value))
		# elif allvar is not None and token.value in allvar:
		# 	print("here")
		# 	print(allvar)
		# 	print(token.type)
		# 	return 'STRING_WITH_VAR'
		# if token.type == "LNUMBER" and RepresentsInt(token.value) and int(token.value) < 10:
		# 	return str(int(token.value))
		return token.type


def sub_token_ast(token, allfunc=None):
	if token in combined :
		return token.upper()
	else:
		return 'FunctionCall'

def sub_token_parent(token):
	if token.value in sql :
		return 'T_SQL'
	elif token.value in input :
		return 'T_INPUT'
	elif token.value in echo :
		return 'T_ECHO'
	elif token.value in (preg + filtering) :
		return 'T_FILTER'
	elif token.value in exec :
		return 'T_EXEC'
	else:
		return token.type



def getReplacedTokens():
	return [x.upper() for x in combined]

def getReplacedTokensLower():
	return [x.lower() for x in combined]

def no_function(data):
	return not any(word in data.lower() for word in getReplacedTokensLower())