#include "Scanner.h"

#include <stdio.h>
#include <malloc.h>
#include <string.h>

int main(int argc, char **argv)
{
	FILE *output;
	char *outputFileName;
	PScanner cpp_scanner;
	PToken token;

	if(argc < 2)
	{
		printf("Usage: scan file_name");
		return 0;
	}

	char *pFile = argv[1];

	cpp_scanner = CreateScanner(pFile);
	if(cpp_scanner == NULL)
	{
		printf("Can not open filename!");
		return 0;
	}

	outputFileName = (char*)malloc(sizeof(char) * (strlen(pFile) + strlen(".scan") + 1));
	sprintf(outputFileName, "%s.scan", pFile);
	output = fopen(outputFileName, "w");
	free(outputFileName);

	int tk_nr_crt = 1;
	while(token = GetNextToken(cpp_scanner))
	{
		fprintf(output, "TOKEN %d:\n", tk_nr_crt++);
		fprintf(output, "Type: %s\n", TokenTypes[token->token_type]);
		fprintf(output, "Attribute: %s\n", (token->token_attribute? token->token_attribute : "NO ATTRIBUTE"));
		fprintf(output, "Line: %d\n\n", token->line);

		delete token;
	}

	printf("Job done!");

	CloseScanner(cpp_scanner);
		
	fclose(output);

	return 0;
}