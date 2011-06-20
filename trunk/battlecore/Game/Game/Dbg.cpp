#ifdef _DEBUG

#include "Dbg.h"

std::ofstream Dbg::logFile;

std::ofstream& Dbg::getLogFile()
{
	return logFile;
}

void Dbg::initDbg()
{
	logFile.open(LOG_FILE, std::ios::out);
}

void Dbg::shutDownDbg()
{
	logFile.close();
}

#endif