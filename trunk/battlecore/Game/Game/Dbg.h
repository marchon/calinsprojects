#ifdef _DEBUG

#pragma once

#include <fstream>
#include "Video.h"

#define LOG_FILE "DBG.log"

class Dbg
{
private:
	static std::ofstream logFile;
protected:
public:
	static std::ofstream& getLogFile();
	static void initDbg();
	static void shutDownDbg();

	/* To be continued... */
};

#endif