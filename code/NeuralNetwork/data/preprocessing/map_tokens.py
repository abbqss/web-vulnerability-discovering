from data.preprocessing import sub_tokens
from torchnlp.encoders import LabelEncoder

reserved = [
    'ARRAY', 'AS', 'BREAK', 'CASE', 'CLASS', 'CONST', 'CONTINUE', 'DECLARE',
    'DEFAULT', 'DIE', 'DO', 'ECHO', 'ELSE', 'ELSEIF', 'EMPTY', 'ENDDECLARE',
    'ENDFOR', 'ENDFOREACH', 'ENDIF', 'ENDSWITCH', 'ENDWHILE', 'EVAL', 'EXIT',
    'EXTENDS', 'FOR', 'FOREACH', 'FUNCTION', 'GLOBAL', 'IF', 'INCLUDE',
    'INCLUDE_ONCE', 'INSTANCEOF', 'ISSET', 'LIST', 'NEW', 'PRINT', 'REQUIRE',
    'REQUIRE_ONCE', 'RETURN', 'STATIC', 'SWITCH', 'UNSET', 'USE', 'VAR',
    'WHILE', 'FINAL', 'INTERFACE', 'IMPLEMENTS', 'PUBLIC', 'PRIVATE',
    'PROTECTED', 'ABSTRACT', 'CLONE', 'TRY', 'CATCH', 'THROW', 'NAMESPACE',
    'FINALLY', 'TRAIT', 'YIELD',
]

# Not used by parser
unparsed = [
    # Invisible characters
    'WHITESPACE',

    # Open and close tags
    'OPEN_TAG', 'OPEN_TAG_WITH_ECHO', 'CLOSE_TAG',

    # Comments
    'COMMENT', 'DOC_COMMENT',
]

tokens = sub_tokens.getReplacedTokens() + reserved + unparsed + [
    # Operators
    'PLUS', 'MINUS', 'MUL', 'DIV', 'MOD', 'AND', 'OR', 'NOT', 'XOR', 'SL',
    'SR', 'BOOLEAN_AND', 'BOOLEAN_OR', 'BOOLEAN_NOT', 'IS_SMALLER',
    'IS_GREATER', 'IS_SMALLER_OR_EQUAL', 'IS_GREATER_OR_EQUAL', 'IS_EQUAL',
    'IS_NOT_EQUAL', 'IS_IDENTICAL', 'IS_NOT_IDENTICAL',

    # Assignment operators
    'EQUALS', 'MUL_EQUAL', 'DIV_EQUAL', 'MOD_EQUAL', 'PLUS_EQUAL',
    'MINUS_EQUAL', 'SL_EQUAL', 'SR_EQUAL', 'AND_EQUAL', 'OR_EQUAL',
    'XOR_EQUAL', 'CONCAT_EQUAL',

    # Increment/decrement
    'INC', 'DEC',

    # Arrows
    'OBJECT_OPERATOR', 'DOUBLE_ARROW', 'DOUBLE_COLON',

    # Delimiters
    'LPAREN', 'RPAREN', 'LBRACKET', 'RBRACKET', 'LBRACE', 'RBRACE', 'DOLLAR',
    'COMMA', 'CONCAT', 'QUESTION', 'COLON', 'SEMI', 'AT', 'NS_SEPARATOR',

    # Casts
    'ARRAY_CAST', 'BINARY_CAST', 'BOOL_CAST', 'DOUBLE_CAST', 'INT_CAST',
    'OBJECT_CAST', 'STRING_CAST', 'UNSET_CAST',

    # Escaping from HTML
    'INLINE_HTML',

    # Identifiers and reserved words
    'DIR', 'FILE', 'LINE', 'FUNC_C', 'CLASS_C', 'METHOD_C', 'NS_C',
    'LOGICAL_AND', 'LOGICAL_OR', 'LOGICAL_XOR',
    'HALT_COMPILER',
    'STRING', 'VARIABLE',
    'LNUMBER', 'DNUMBER', 'NUM_STRING',
    'CONSTANT_ENCAPSED_STRING', 'ENCAPSED_AND_WHITESPACE', 'QUOTE',
    'DOLLAR_OPEN_CURLY_BRACES', 'STRING_VARNAME', 'CURLY_OPEN',

    # Heredocs
    'START_HEREDOC', 'END_HEREDOC',

    # Nowdocs
    'START_NOWDOC', 'END_NOWDOC',

    # Backtick
    'BACKTICK',

    #START for pdg
    'START',

] + ['VAR' + str(i) for i in range(10)] + [str(i) for i in range(10)]

encoder = LabelEncoder(tokens)
max_len = 20
def tokenise(input):
    output = []
    i = 0
    for token in input:
        if i >= max_len:
            break
        enc = encoder.encode(token)
        if enc == 0:
            print("error")
            print(token)
            exit(1)
        output.append(enc)
        i+=1
    if i < max_len:
        for j in range(max_len - i):
            output.append(0)
    return output