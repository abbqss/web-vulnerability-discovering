
package taintAnalysis;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Abeer Alhuzali 
 * Some lists are adopted from RIPS by Johannes Dahse (johannes.dahse@rub.de) 
 *
 */



public class Sanitization{

	
		
		// ALL vulnerabilities sanitization functions 
		 public final static String[] F_SANITIZATION_STRING = {
			"intval",
			"floatval",
			"doubleval",
			"filter_input",
			"urlencode",
			"rawurlencode",
			"round",
			"floor",
			"strlen",
			"strrpos",
			"strpos",
			"strftime",
			"strtotime",
			"md5",
			"md5_file",
			"sha1",
			"sha1_file",
			"crypt",
			"crc32",
			"hash",
			"mhash",
			"hash_hmac",
			"password_hash",
			"mcrypt_encrypt",
			"mcrypt_generic",
			"base64_encode",
			"ord",
			"sizeof",
			"count",
			"bin2hex",
			"levenshtein",
			"abs",
			"bindec",
			"decbin",
			"dechex",
			"decoct",
			"hexdec",
			"rand",
			"max",
			"min",
			"metaphone",
			"tempnam",
			"soundex",
			"money_format",
			"number_format",
			"date_format",
			"filetype",
			"nl_langinfo",
			"bzcompress",
			"convert_uuencode",
			"gzdeflate",
			"gzencode",
			"gzcompress",
			"http_build_query",
			"lzf_compress",
			"zlib_encode",
			"imap_binary",
			"iconv_mime_encode",
			"bson_encode",
			"sqlite_udf_encode_binary",
			"session_name",
			"readlink",
			"getservbyport",
			"getprotobynumber",
			"gethostname",
			"gethostbynamel",
			"gethostbyname",
			"date"
		 };
		
		
		// XSS sanitization functions 
		 public final static String[] F_SANITIZATION_XSS = {
			"htmlentities",
			"htmlspecialchars",
			"highlight_string"
            };	
		
		// SQLI sanitization functions 
		 public final static String[] F_SANITIZATION_SQL = {
			"addslashes",
			"dbx_escape_string",
			"db2_escape_string",
			"ingres_escape_string",
			"maxdb_escape_string",
			"maxdb_real_escape_string",
			"mysql_escape_string",
			"mysql_real_escape_string",
			"mysqli_escape_string",
			"mysqli_real_escape_string",
			
		 };	
		
		 //special cases where some functions can be used as sanitizers
		 //if  the  flag  ENT_QUOTES is set
		 public final static String[] F_SANITIZATION_SQL_SPECIAL ={
				 "htmlentities",
				 "htmlspecialchars" 
		 };
		 

		
		// file handling sanitization functions 
		 public final static String[] F_SANITIZATION_FILE = {
			"basename",
			"dirname",
			"pathinfo"
		 };
		
		// command execution Sanitization functions 
		 public final static String[] F_SANITIZATION_SYSTEM = {
			"escapeshellarg",
			"escapeshellcmd"
		 };	
		
	
		
		 
		// sanitization functions in conditional statements  
				 public final String[] F_SANITIZATION_BOOL = {"is_bool",
					"is_double",
					"is_float",
					"is_real",
					"is_long",
					"is_int",
					"is_integer",
					"is_null",
					"is_numeric",
					"is_finite",
					"is_infinite",
					"ctype_alnum",
					"ctype_alpha",
					"ctype_cntrl",
					"ctype_digit",
					"ctype_xdigit",
					"ctype_upper",
					"ctype_lower",
					"ctype_space",
					"in_array",
					"preg_match",
					"preg_match_all",
					"fnmatch",
					"ereg",
					"eregi"};

		public static boolean find(String[] searchIn, String funcName) {
			for (String s : searchIn){
				if (s.equals(funcName))
					return true;
			}
			return false;
		}	
		
		
			
	
}
