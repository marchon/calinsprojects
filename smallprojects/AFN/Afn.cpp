#include "Afn.h"

Afn::Afn(ifstream &file)
{
	char ch;

	file >> initial_state;

	do
	{
		char state, symbol;
		vector<char> states;

		file >> ch;
		if(ch == '.')
			break;

		state = ch;
		file >> symbol;
		
		do
		{
			file >> ch;
			if(ch != '.') 
				states.push_back(ch);
		}while(ch != '.');

		delta_table[state][symbol] = states;
	}while(true);
	

	while(file >> ch)
		final_states.push_back(ch);

}

bool Afn::CheckWord(vector<char> &word)
{
	return check(initial_state, 0, word);
}

bool Afn::check(char cur_state, int cur_symbol, vector<char> &word)
{
	if(cur_symbol == word.size())
	{
		for(int i = 0; i < final_states.size(); i++)
			if(final_states[i] == cur_state)
				return true;
		
		vector<char> v = delta_table[cur_state]['\\'];

		for(int i = 0; i < v.size(); i++)
			if(v[i] != cur_state)
				if(check(v[i], cur_symbol, word))
					return true;

		return false;
	}

	//lambda transition
	vector<char> v = delta_table[cur_state]['\\'];

	for(int i = 0; i < v.size(); i++)
		if(v[i] != cur_state)
			if(check(v[i], cur_symbol, word))
				return true;

	v = delta_table[cur_state][word[cur_symbol]];
	
	for(int i = 0; i < v.size(); i++)
	{
			if(check(v[i], cur_symbol + 1, word))
				return true;
	}

	return false;
}