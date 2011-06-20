#pragma once

#include <stdio.h>
#include <malloc.h>
#include <string.h>

#include <vector>
using namespace std;

typedef enum
{
	ASM = 0, AUTO, BOOL, BREAK, CASE, CATCH, CHAR, CLASS, CONST, CONST_CAST, CONTINUE, DEFAULT, DELETE, DO, 
	DOUBLE, DYNAMIC_CAST, ELSE, ENUM, EXPLICIT, EXPORT, EXTERN, FALSE, FLOAT, FOR, FRIEND, GOTO, IF, INLINE, 
	INT, LONG, MUTABLE, NAMESPACE, NEW, OPERATOR, PRIVATE, PROTECTED, PUBLIC, REGISTER, REINTERPRET_CAST, 
	RETURN, SHORT, SIGNED, SIZEOF, STATIC, STATIC_CAST, STRUCT, SWITCH, TEMPLATE, THIS, THROW, TRUE, TRY,
	TYPEDEF, TYPEID, TYPENAME, UNION, UNSIGNED, USING, VIRTUAL, VOID, VOLATILE, WCHAR_T, WHILE, //key words

	LOGICAL_NOT /* ! */, MOD /* % */, BITEWISE_XOR /* ^ */, BITEWISE_NOT /* ~ */, AND /* & */, 
	STAR /* * */, PLUS /* + */, MINUS /* - */, BECOMES /* = */, BITEWISE_OR /* | */,
	LESS /* < */, GREATER /* > */, SLASH /* / */, QUESTION_MARK /* ? */, BACKSLASH /* \ */,
	COLON /* : */, SEMICOLON /* ; */, DOT /* . */, COMMA /* , */, LPAR /* ( */, RPAR /* ) */, LCURLY /* { */,
	RCURLY /* } */, LSQUARE /* [ */, RSQUARE /* ] */,  

	SCOPE_RESOLUTION /* :: */, INCREMENT /* ++ */, DECREMENT /* -- */, ARROW /* -> */, LOGICAL_AND /* && */,
	LOGICAL_OR /* || */, EQUALITY_TEST /* == */, NOT_EQUALITY_TEST /* != */, POINTER_TO_MEMBER /* .* */,  
	RSHIFT /* >> */, LSHIFT /* << */,  LESS_OR_EQUAL /* <= */,  GREATER_OR_EQUAL /* >= */,
	PLUS_EQUALS /* += */, MINUS_EQUALS /* -= */, STAR_EQUALS /* *= */, SLASH_EQUALS /* /= */,
	MOD_EQUALS /* %= */, AND_EQUALS /* &= */, XOR_EQUALS /* ^= */, OR_EQUALS /* |= */,

	RSHIFT_EQUALS /* >>= */, LSHIFT_EQUALS /* <<= */, POINTER_POINTER_TO_MEMBER /* ->* */,
	IDENTIFIER,
	LITERAL_INTEGER_CONSTANT,
	LITERAL_FLOATINGPOINT_CONSTANT,
	LITERAL_CHARACTER_CONSTANT,
	LITERAL_STRING_CONSTANT,
	COMMENT,
	ERROR
} Token_Type;

extern char *TokenTypes[];

typedef struct _Scanner
{
	char *filename;
	FILE *input;

	int curr_ch;
	int curr_line;

	vector<char *> attributes;
} Scanner, *PScanner;

typedef struct _Token
{
	Token_Type token_type;
	char *token_attribute;
	int line;
} Token, *PToken;

PScanner CreateScanner(char *);
void CloseScanner(PScanner);
PToken GetNextToken(PScanner);