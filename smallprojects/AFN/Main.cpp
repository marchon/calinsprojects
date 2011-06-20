#include "Afn.h"

int main(int argc, char **argv)
{

	ifstream file("Afn.txt");

	Afn afn(file);

	file.close();

	cout<<"Word ceck(type . when finished):\n";

	char ch;
	vector<char> word;
	
	do
	{
		word.clear();
		while((ch = cin.get()) != '\n')
		{
			if(ch == '.')
				return 0;

			word.push_back(ch);
		}
		if(afn.CheckWord(word))
			cout<<"Accepted!\n";
		else 
			cout<<"Not accepted!\n";

	}while(true);

	return 0;
}