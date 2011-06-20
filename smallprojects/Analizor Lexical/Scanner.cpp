#include "Scanner.h"

#define MAX_SIZE			50
#define MAX_SIZE_INCREMENT	50

#define MAKE_TOKEN(token_name, type, attr, ln)		{	\
														token_name = new Token;\
														token_name->token_type = type;\
														token_name->token_attribute = attr;\
														token_name->line = ln;\
													}


	

char *TokenTypes[] = 
{
	"asm", "auto", "bool", "break", "case", "catch", "char", "class", "const", "const_cast", 
	"continue", "default", "delete", "do", "double", "dynamic_cast", "else", "enum", "explicit", 
	"export", "extern", "false", "float", "for", "friend", "goto", "if", "inline", "int", "long", "mutable",
	"namespace", "new", "operator", "private", "protected", "public", "register", "reinterpret_cast", 
	"return", "short", "signed", "sizeof", "static", "static_cast", "struct", "switch", "template", "this", 
	"throw", "true", "try", "typedef", "typeid", "typename", "union", "unsigned", "using", "virtual",
	"void", "volatile", "wchar_t", "while",

	
	"logical_not !" /* ! */, "mod %" /* % */, "bitewise_xor ^" /* ^ */, "bitewise_not ~" /* ~ */, "and &" /* & */, 
	"star *" /* * */, "plus +" /* + */, "minus -" /* - */, "becomes =" /* = */, "bitewise_or |" /* | */,
	"less <" /* < */, "greater >" /* > */, "slash /" /* / */, "question_mark ?" /* ? */, "backslash \\" /* \ */,
	"colon :" /* : */, "semicolon ;" /* ; */, "dot ." /* . */, "comma ," /* , */, "lpar (" /* ( */, 
	"rpar )" /* ) */, "lcurly {" /* { */, "rcurly }" /* } */, "lsquare [" /* [ */, "rsquare ]" /* ] */,  

	"scope_resolution ::" /* :: */, "increment ++" /* ++ */, "decrement --" /* -- */, "arrow ->" /* -> */, 
	"logical_and &&" /* && */, "logical_or ||" /* || */, "equality_test ==" /* == */, "not_equality_test !=" /* != */, 
	"pointer_to_member .*" /* .* */, "rshift >>" /* >> */, "lshift <<" /* << */,  "less_or_equal <=" /* <= */, 
	"greater_or_equal >=" /* >= */, "plus_equals +=" /* += */, "minus_equals -=" /* -= */, "star_equals *=" /* *= */, 
	"slash_equals /=" /* /= */, "mod_equals %=" /* %= */, "and_equals &=" /* &= */, "xor_equals ^=" /* ^= */, "or_equals |=" /* |= */,

	"rshift_equals >>=" /* >>= */, "lshift_equals <<=" /* <<= */, "pointer_pointer_to_member ->*" /* ->* */,

	"identifier", 
	"integer constant",
	"floating point constant",
	"character constant",
	"string constant",
	"comment",
	"parsing error"
};

void getNextChar(PScanner scanner)
{
	scanner->curr_ch = fgetc(scanner->input);

	if(scanner->curr_ch == '\n')
		scanner->curr_line ++;
}


void jumpWhiteSpaces(PScanner scanner)
{
	while(scanner->curr_ch < '!' && scanner->curr_ch != EOF)
	{
		getNextChar(scanner);
	}
}

void readUntilWhiteSpace(PScanner scanner)
{
	while(scanner->curr_ch >= '!' && scanner->curr_ch != EOF)
	{
		getNextChar(scanner);
	}
}

PScanner CreateScanner(char *file_path)
{
	PScanner scanner = new Scanner;
	if(scanner == NULL)
		return NULL;

	scanner->filename = new char[(strlen(file_path) + 1)];
	if(scanner->filename == NULL)
	{
		delete scanner;
		return NULL;
	}

	memcpy(scanner->filename, file_path, strlen(file_path) + 1);

	scanner->input = fopen(scanner->filename, "r");
	if(scanner->input == NULL)
	{
		delete[] scanner->filename;
		delete scanner;
		return NULL;
	}

	scanner->curr_line = 1;

	getNextChar(scanner);
	
	return scanner;
}

void CloseScanner(PScanner scanner)
{;
	for(size_t i = 0; i < scanner->attributes.size(); i++)
		delete[] scanner->attributes[i];
	fclose(scanner->input);
	delete[] (scanner->filename);
	delete (scanner);
}

PToken GetNextToken(PScanner scanner)
{

	int state = 0;
	bool isFinalState = false;

	Token_Type type = ERROR; 

	static char token_value[500];
	int pos = 0;

	int startLine = 0;

	PToken token = NULL;

	//jumpWhiteSpaces(scanner);
	while(1)
	{
		if(pos == 500)
		{
			MAKE_TOKEN(token, ERROR, NULL, startLine);
			readUntilWhiteSpace(scanner);
			isFinalState = true;
		}
		else			
		switch(state)
		{
		case 0:	//stare initiala
			jumpWhiteSpaces(scanner);
			startLine = scanner->curr_line;
			switch(scanner->curr_ch)
			{
			case '/':
				type = COMMENT;
				//token_value[pos ++] = scanner->curr_ch;
				state = 1;
				break;
			case '"':
				state = 7;
				type = LITERAL_STRING_CONSTANT;
				break;
			case '_':
				state = 9;
				token_value[pos ++] = scanner->curr_ch;
				break;
			case '\'':
				state = 26;
				break;
			/* operators */
			case '%':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, MOD_EQUALS, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, MOD, NULL, startLine);
				}
				isFinalState = true;
				break;
			case '^':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, XOR_EQUALS, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, BITEWISE_XOR, NULL, startLine);
				}
				isFinalState = true;
				break;
			case '&':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, AND_EQUALS, NULL, startLine);
					getNextChar(scanner);
				}
				else if(scanner->curr_ch == '&')
				{
					MAKE_TOKEN(token, LOGICAL_AND, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, AND, NULL, startLine);
				}
				isFinalState = true;
				break;
			case '*':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, STAR_EQUALS, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, STAR, NULL, startLine);
				}
				isFinalState = true;
				break;
			case '-':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, MINUS_EQUALS, NULL, startLine);
					getNextChar(scanner);
				}
				else if(scanner->curr_ch == '-')
				{
					MAKE_TOKEN(token, DECREMENT, NULL, startLine);
					getNextChar(scanner);
				}
				else if(scanner->curr_ch == '>')
				{
					getNextChar(scanner);
					if(scanner->curr_ch == '*')
					{
						MAKE_TOKEN(token, POINTER_POINTER_TO_MEMBER, NULL, startLine);
						getNextChar(scanner);
					}
					else
					{
						MAKE_TOKEN(token, ARROW, NULL, startLine);
					}
				}
				else
				{
					MAKE_TOKEN(token, MINUS, NULL, startLine);
				}
				isFinalState = true;
				break;
			case '+':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, PLUS_EQUALS, NULL, startLine);
					getNextChar(scanner);
				}
				else if(scanner->curr_ch == '+')
				{
					MAKE_TOKEN(token, INCREMENT, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, PLUS, NULL, startLine);
				}
				isFinalState = true;
				break;
			case '=':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, EQUALITY_TEST, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, BECOMES, NULL, startLine);
				}
				isFinalState = true;
				break;
			case '~':
				getNextChar(scanner);
				MAKE_TOKEN(token, BITEWISE_NOT, NULL, startLine);
				isFinalState = true;
				break;
			case '|':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, OR_EQUALS, NULL, startLine);
					getNextChar(scanner);
				}
				else if(scanner->curr_ch == '|')
				{
					MAKE_TOKEN(token, LOGICAL_OR, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, BITEWISE_OR, NULL, startLine);
				}
				isFinalState = true;
				break;
			case '.':
				getNextChar(scanner);
				if(scanner->curr_ch == '*')
				{
					MAKE_TOKEN(token, POINTER_TO_MEMBER, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, DOT, NULL, startLine);
				}
				isFinalState = true;
				break;
			case '<':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, LESS_OR_EQUAL, NULL, startLine);
					getNextChar(scanner);
				}
				else if(scanner->curr_ch == '<')
				{
					getNextChar(scanner);
					if(scanner->curr_ch == '=')
					{
						MAKE_TOKEN(token, LSHIFT_EQUALS, NULL, startLine);
						getNextChar(scanner);
					}
					else
					{
						MAKE_TOKEN(token, LSHIFT, NULL, startLine);
					}
				}
				else
				{
					MAKE_TOKEN(token, LESS, NULL, startLine);
				}
				break;
			case '>':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, GREATER_OR_EQUAL, NULL, startLine);
					getNextChar(scanner);
				}
				else if(scanner->curr_ch == '>')
				{
					getNextChar(scanner);
					if(scanner->curr_ch == '=')
					{
						MAKE_TOKEN(token, RSHIFT_EQUALS, NULL, startLine);
						getNextChar(scanner);
					}
					else
					{
						MAKE_TOKEN(token, RSHIFT, NULL, startLine);
					}
				}
				else
				{
					MAKE_TOKEN(token, GREATER, NULL, startLine);
				}
				break;
			/*case '/':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, SLASH_EQUALS, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, SLASH, NULL, startLine);
				}
				isFinalState = true;
				break;*/
			case '?':
				getNextChar(scanner);
				MAKE_TOKEN(token, QUESTION_MARK, NULL, startLine);
				isFinalState = true;
				break;
			case ':':
				getNextChar(scanner);
				if(scanner->curr_ch == ':')
				{
					MAKE_TOKEN(token, SCOPE_RESOLUTION, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, COLON, NULL, startLine);
				}
				isFinalState = true;
				break;
			case ';':
				getNextChar(scanner);
				MAKE_TOKEN(token, SEMICOLON, NULL, startLine);
				isFinalState = true;
				break;
			case '{':
				getNextChar(scanner);
				MAKE_TOKEN(token, LCURLY, NULL, startLine);
				isFinalState = true;
				break;
			case '}':
				getNextChar(scanner);
				MAKE_TOKEN(token, RCURLY, NULL, startLine);
				isFinalState = true;
				break;
			case '(':
				getNextChar(scanner);
				MAKE_TOKEN(token, LPAR, NULL, startLine);
				isFinalState = true;
				break;
			case ')':
				getNextChar(scanner);
				MAKE_TOKEN(token, RPAR, NULL, startLine);
				isFinalState = true;
				break;
			case '[':
				getNextChar(scanner);
				MAKE_TOKEN(token, LSQUARE, NULL, startLine);
				isFinalState = true;
				break;
			case ']':
				getNextChar(scanner);
				MAKE_TOKEN(token, RSQUARE, NULL, startLine);
				isFinalState = true;
				break;
			case '!':
				getNextChar(scanner);
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, NOT_EQUALITY_TEST, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, LOGICAL_NOT, NULL, startLine);
				}
				isFinalState = true;
				break;
			//case '\':
			//	break;
			case ',':
				getNextChar(scanner);
				MAKE_TOKEN(token, COMMA, NULL, startLine);
				isFinalState = true;
				break;
			/* operators */

			default:
				if((scanner->curr_ch >= 'A' && scanner->curr_ch <= 'Z') ||
					(scanner->curr_ch >= 'a' && scanner->curr_ch <= 'z'))
				{
					state = 9;
					token_value[pos ++] = scanner->curr_ch;
				}
				else if(scanner->curr_ch >= '0' && scanner->curr_ch <= '9')
				{
					if(scanner->curr_ch == '0')
						state = 12;
					else
						state = 11;

					token_value[pos ++] = scanner->curr_ch;
				}
				else if(scanner->curr_ch == EOF)
					return NULL;
				else
				{
					//todo...error
					MAKE_TOKEN(token, ERROR, NULL, startLine);
					readUntilWhiteSpace(scanner);
					isFinalState = true;
				}
			}
			break;
		case 1: //comment
			getNextChar(scanner);
			//token_value[pos ++] = scanner->curr_ch;
			if(scanner->curr_ch == '/')
				state = 2;
			else if(scanner->curr_ch == '*')
				state = 5;
			else //operator / or /=
			{
				if(scanner->curr_ch == '=')
				{
					MAKE_TOKEN(token, SLASH_EQUALS, NULL, startLine);
					getNextChar(scanner);
				}
				else
				{
					MAKE_TOKEN(token, SLASH, NULL, startLine);
				}
				isFinalState = true;
			}
			break;
		case 2: //linecomment
			getNextChar(scanner);
			switch(scanner->curr_ch)
			{
			case EOF:
			case '\n':
			case '\v':
			case '\f':
				state = 3;
				break;
			case '\\':
				state = 4;
				break;
			default:
				token_value[pos ++] = scanner->curr_ch;
			}
			break;
		case 3:	//finish comment or string
			{
				getNextChar(scanner);

				char *value = new char[pos + 1];
				memcpy(value, token_value, pos);
				value[pos] = '\0';
				scanner->attributes.push_back(value);
				
				MAKE_TOKEN(token, type, value, startLine);

				isFinalState = true;
			}
			break;
		case 4:
			getNextChar(scanner);
			if(scanner->curr_ch != '\\')
			{
				token_value[pos ++] = ' ';
				state = 2;
			}
			break;
		case 5: //multiple line comments
			getNextChar(scanner);
			switch(scanner->curr_ch)
			{
			case '*':
				state = 6;
				break;
			case '\n':
				//alte caractere escape
				break;
			default:
				token_value[pos ++] = scanner->curr_ch;
			}
			break;
		case 6: //multiple line posible end
			getNextChar(scanner);
			switch(scanner->curr_ch)
			{
			case '*':
				token_value[pos ++] = scanner->curr_ch;
				break;
			case '/':
				state = 3;
				break;
			default:
				token_value[pos ++] = '*';
				if(scanner->curr_ch != '\n')
				token_value[pos ++] = scanner->curr_ch;
				state = 5;
			}
			break;
		case 7:
			getNextChar(scanner);
			if(scanner->curr_ch < ' ')
				{
					MAKE_TOKEN(token, ERROR, NULL, startLine);
					readUntilWhiteSpace(scanner);
					isFinalState = true;
				}
			switch(scanner->curr_ch)
			{
			case '\\':
				state = 8;
				break;
			case '"':
				state = 3;
				break;
			default:
				token_value[pos ++] = scanner->curr_ch;
			}
			break;
		case 8:
			getNextChar(scanner);
			if(scanner->curr_ch >= ' ')
			{
				token_value[pos ++] = '\\';
				token_value[pos ++] = scanner->curr_ch;
			}
			else
				token_value[pos ++] = ' ';
			state = 7;
			break;
		case 9:
			getNextChar(scanner);
			if((scanner->curr_ch >= 'A' && scanner->curr_ch <= 'Z') ||
				(scanner->curr_ch >= 'a' && scanner->curr_ch <= 'z') ||
				(scanner->curr_ch >= '0' && scanner->curr_ch <= '9') ||
				scanner->curr_ch == '_')
				token_value[pos ++] = scanner->curr_ch;
			else
				state = 10;
			break;
		case 10:
			token_value[pos] = '\0';
			int i;
			for(i = 0; i <= WHILE; i++)
				if(!strcmp(token_value, TokenTypes[i]))
				break;


			if(i <= WHILE)
			{
				MAKE_TOKEN(token, ((Token_Type)i), NULL, startLine);
			}
			else
			{
				char *value = new char[pos + 1];
				memcpy(value, token_value, pos + 1);
				scanner->attributes.push_back(value);

				MAKE_TOKEN(token, IDENTIFIER, value, startLine);
			}

			isFinalState = true;
			break;
		case 11: //integer dec
		case 17: //hex
		case 18: //oct
			getNextChar(scanner);
			if((state == 11 && scanner->curr_ch >= '0' && scanner->curr_ch <= '9')||
				(state == 18 && scanner->curr_ch >= '0' && scanner->curr_ch <= '7')||
				(state == 17 && ((scanner->curr_ch >= '0' && 
				scanner->curr_ch <= '9') || (scanner->curr_ch >= 'a' && scanner->curr_ch <= 'f') ||
				(scanner->curr_ch >= 'A' && scanner->curr_ch <= 'F'))))
			{
				token_value[pos ++] = scanner->curr_ch;
			}
			else if(state == 11 && (scanner->curr_ch == '.' || scanner->curr_ch == 'e' || scanner->curr_ch == 'E')) //float
			{
				token_value[pos ++] = scanner->curr_ch;
				if(scanner->curr_ch == '.')
					state = 20;
				else state = 21;
			}
			else if(scanner->curr_ch == 'u'||scanner->curr_ch == 'U')
			{
				token_value[pos ++] = scanner->curr_ch;
				state = 13;
			}
			else if(scanner->curr_ch == 'l' || scanner->curr_ch == 'L')
			{
				token_value[pos ++] = scanner->curr_ch;
				state = 14;
			}
			else
				state = 16;			
			break;
		case 12: 
			getNextChar(scanner);
			if(scanner->curr_ch == 'x' || scanner->curr_ch == 'X')
			{
				token_value[pos ++] = scanner->curr_ch;
				state = 19;
			}
			else if(scanner->curr_ch >= '0' && scanner->curr_ch <= '7')
			{
				token_value[pos ++] = scanner->curr_ch;
				state = 18;
			}
			else if(scanner->curr_ch == '.' || scanner->curr_ch == 'e' || scanner->curr_ch == 'E') //float
			{
				token_value[pos ++] = scanner->curr_ch;
				if(scanner->curr_ch == '.')
					state = 20;
				else state = 21;
			}
			else
				state = 16;
			break;
		case 13:
			getNextChar(scanner);
			if(scanner->curr_ch == 'l' || scanner->curr_ch == 'L')
			{
				token_value[pos ++] = scanner->curr_ch;
				state = 15;
			}
			else
				state = 16;
			break;
		case 14:
			getNextChar(scanner);
			if(scanner->curr_ch == 'u' || scanner->curr_ch == 'U')
			{
				token_value[pos ++] = scanner->curr_ch;
				state = 15;
			}
			else
				state = 16;
		case 15:
		case 16:
		case 24:
		case 25:
			{
				if(state == 15 || state == 24)
					getNextChar(scanner);

				char *value = new char[pos + 1];
				memcpy(value, token_value, pos);
				value[pos] = '\0';
				scanner->attributes.push_back(value);
				
				if(state == 15 || state == 16)
				{
					MAKE_TOKEN(token, LITERAL_INTEGER_CONSTANT, value, startLine);
				}
				else
				{
					MAKE_TOKEN(token, LITERAL_FLOATINGPOINT_CONSTANT, value, startLine);
				}

				isFinalState = true;
			}
			break;
		case 19://at least one hex digit after 0x
			getNextChar(scanner);
			if((scanner->curr_ch >= '0' && 
				scanner->curr_ch <= '9') || (scanner->curr_ch >= 'a' && scanner->curr_ch <= 'f') ||
				(scanner->curr_ch >= 'A' && scanner->curr_ch <= 'F'))
			{
				token_value[pos ++] = scanner->curr_ch;
				state = 17;
			}
			else
			{
				MAKE_TOKEN(token, ERROR, NULL, startLine);
				readUntilWhiteSpace(scanner);
				isFinalState = true;
			}
			break;
		case 20:
		case 22:
			getNextChar(scanner);
			if(scanner->curr_ch >= '0' && scanner->curr_ch <= '9')
				token_value[pos ++] = scanner->curr_ch;
			else if(scanner->curr_ch == 'f' || scanner->curr_ch == 'l' ||
				scanner->curr_ch == 'F' || scanner->curr_ch == 'L')
			{
				token_value[pos ++] = scanner->curr_ch;
				state = 25;
			}
			else if(state == 20 && (scanner->curr_ch == 'e' || scanner->curr_ch == 'E'))
			{
				token_value[pos ++] = scanner->curr_ch;
				state = 21;
			}
			else 
				state = 24;
			break;
		case 21:
			getNextChar(scanner);
			token_value[pos ++] = scanner->curr_ch;
			if(scanner->curr_ch == '+' || scanner->curr_ch == '-')
				state = 23;
			else if(scanner->curr_ch >= '0' && scanner->curr_ch <= '9')
				state = 22;
			else
			{
				MAKE_TOKEN(token, ERROR, NULL, startLine);
				readUntilWhiteSpace(scanner);
				isFinalState = true;
			}
			break;
		case 23:
			getNextChar(scanner);
			token_value[pos ++] = scanner->curr_ch;
			if(scanner->curr_ch >= '0' && scanner->curr_ch <= '9')
				state = 22;
			else
			{
				MAKE_TOKEN(token, ERROR, NULL, startLine);
				readUntilWhiteSpace(scanner);
				isFinalState = true;
			}
			break;
		case 26: //character constant
			getNextChar(scanner);
			token_value[pos ++] = scanner->curr_ch;
			if(scanner->curr_ch == '\\')
				state = 28;
			else if(scanner->curr_ch != '\n' && scanner->curr_ch != '\'')
				state = 27;
			else
			{
				MAKE_TOKEN(token, ERROR, NULL, startLine);
				readUntilWhiteSpace(scanner);
				isFinalState = true;
			}
			break;
		case 27:
			getNextChar(scanner);
			//token_value[pos ++] = scanner->curr_ch;
			if(scanner->curr_ch == '\'')
				state = 29;
			else
			{
				MAKE_TOKEN(token, ERROR, NULL, startLine);
				readUntilWhiteSpace(scanner);
				isFinalState = true;
			}
			break;
		case 28:
			getNextChar(scanner);
			token_value[pos ++] = scanner->curr_ch;
			switch(scanner->curr_ch)
			{
			case 'n':
			case 't':
			case 'v':
			case 'b':
			case 'r':
			case 'f':
			case 'a':
			case '\\':
			case '?':
			case '\'':
			case '\"':
				state = 27;
				break;
			case 'x':
				state = 30;
				break;
			default:
				if(scanner->curr_ch >= '0' && scanner->curr_ch <= '8')
					state = 31;
				else 
				{
					MAKE_TOKEN(token, ERROR, NULL, startLine);
					readUntilWhiteSpace(scanner);
					isFinalState = true;
				}
			}
			break;
		case 29:
			{
				char *value = new char[pos + 1];
				memcpy(value, token_value, pos);
				value[pos] = '\0';
				scanner->attributes.push_back(value);

				MAKE_TOKEN(token, LITERAL_CHARACTER_CONSTANT, value, startLine);

				getNextChar(scanner);
				isFinalState = true;
			}
			break;
		case 30: //hex ascii value
			{
				static int cnt = 0;

				if(cnt++ == 3)
				{
					cnt = 0;
					state = 27;
				}
				else
				{
					getNextChar(scanner);
					token_value[pos ++] = scanner->curr_ch;

					if((scanner->curr_ch >= '0' && scanner->curr_ch <= '9') || 
						(scanner->curr_ch >= 'a' && scanner->curr_ch <= 'f') ||
						(scanner->curr_ch >= 'A' && scanner->curr_ch <= 'F'))
						;
					else if(scanner->curr_ch == '\'')
					{
						state = 29;
						cnt = 0;
					}
					else
					{
						MAKE_TOKEN(token, ERROR, NULL, startLine);
						readUntilWhiteSpace(scanner);
						isFinalState = true;
						cnt = 0;
					}

				}
			}
			break;
		case 31: //oct ascii value
			{
				static int cnt = 0;

				if(cnt++ == 2)
				{
					cnt = 0;
					state = 27;
				}
				else
				{
					getNextChar(scanner);
					
					if((scanner->curr_ch >= '0' && scanner->curr_ch <= '7'))
						token_value[pos ++] = scanner->curr_ch;
					else if(scanner->curr_ch == '\'')
					{
						state = 29;
						cnt = 0;
					}
					else
					{
						MAKE_TOKEN(token, ERROR, NULL, startLine);
						readUntilWhiteSpace(scanner);
						isFinalState = true;
						cnt = 0;
					}
				}
			}
			break;
		}

		if(isFinalState)
			break;
		if(scanner->curr_ch == EOF)
			isFinalState = true;
	}

	return token;
}

