#include <fstream>
#include <iostream>

#include <map>
#include <vector>
using namespace std;

/*
	Input file(lambda transition is '\'):

	q			 -- initial state

	qp sk qi1 ___ qim. --- transition function(where sk in {s1, s2, ..., sn} U {\}
	|
	|
	|

	.qk1 ___ qks --- final states
*/

class Afn
{
public:
	Afn(ifstream &);
	bool CheckWord(vector<char> &);

private:

	bool check(char, int, vector<char> &);

	map<char, map<char, vector<char>>> delta_table;
	vector<char> final_states;
	char initial_state;
};