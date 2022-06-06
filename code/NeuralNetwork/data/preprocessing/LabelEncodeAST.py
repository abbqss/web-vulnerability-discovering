from torchnlp.encoders import LabelEncoder
from data.preprocessing import sub_tokens

alltokens = ['InlineHTML','Block','Assignment','ListAssignment','New','Clone','Break','Continue','Return','Yield','Global',
             'Static','Echo','Print','Unset','Try','Catch','Finally','Throw','Declare','Directive','Function','Method','Closure',
             'Class','Trait','ClassConstants','ClassConstant','ClassVariables','ClassVariable','Interface','AssignOp',
             'BinaryOp','UnaryOp','TernaryOp','PreIncDecOp','PostIncDecOp','Cast','IsSet','Empty','Eval',
             'Include','Require','Exit','Silence','MagicConstant','Constant','Variable','StaticVariable','LexicalVariable',
             'FormalParameter','Parameter','FunctionCall','Array','ArrayElement','ArrayOffset','StringOffset','ObjectProperty',
             'StaticProperty','MethodCall','StaticMethodCall','If','ElseIf','Else','While','DoWhile','For','Foreach',
             'ForeachVariable','Switch','Case','Default','Namespace','UseDeclarations','UseDeclaration','ConstantDeclarations',
             'ConstantDeclaration','TraitUse','TraitModifier', 'str','int','float','char','double', 'NoneType'] \
            + sub_tokens.getReplacedTokens()

encoder = LabelEncoder(alltokens)

def encode(token):
    return encoder.encode(token)