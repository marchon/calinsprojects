import sys
import re

comp={
	#A
	"0":"0101010",
	"1":"0111111",
	"-1":"0111010",
	"D":"0001100",
	"A":"0110000",
	"!D":"0001101",
	"!A":"0110001",
	"-D":"0001111",
	"-A":"0110011",
	"D+1":"0011111",
	"A+1":"0110111",
	"D-1":"0001110",
	"A-1":"0110010",
	"D+A":"0000010",
	"D-A":"0010011",
	"A-D":"0000111",
	"D&A":"0000000",
	"D|A":"0010101",

	#M
	"M":"1110000",
	"!M":"1110001",
	"-M":"1110011",
	"M+1":"1110111",
	"M-1":"1110010",
	"D+M":"1000010",
	"D-M":"1010011",
	"M-D":"1000111",
	"D&M":"1000000",
	"D|M":"1010101"
}

dest={
	None:"000",
	"M":"001",
	"D":"010",
	"MD":"011",
	"A":"100",
	"AM":"101",	
	"AD":"110",
	"AMD":"111"
}

jump={
	None:"000",
	"JGT":"001",
	"JEQ":"010",
	"JGE":"011",
	"JLT":"100",
	"JNE":"101",
	"JLE":"110",
	"JMP":"111"
}

symb={
	"SP":"0",
	"LCL":"1",
	"MRG":"10",
	"THIS":"11",
	"THMT":"100",
	"R0":"0",
	"R1":"1",
	"R2":"10",
	"R3":"11",
	"R4":"100",
	"R5":"101",
	"R6":"110",
	"R7":"111",
	"R8":"1000",
	"R9":"1001",
	"R10":"1010",
	"R11":"1011",
	"R12":"1100",
	"R13":"1101",
	"R14":"1110",
	"R15":"1111",
	"SCREEN":"100000000000000",
	"KBD":"110000000000000"
}

def prepared(lines):
	"""Eliminate white spaces and comments"""

	for line in lines:
		line = re.sub("//.*", "", line).strip()
		if len(line) > 0:
			yield line

def get_variable_symbol(instr):
	"""Returns the variable name in an A-instruction, if the case"""
	if re.match("@[a-zA-Z_\.\$:][a-zA-Z0-9_\.\$:]*", instr) is not None:
		return instr[1:]
	return None

def get_label_symbol(instr):
	"""Returns the label name, if this is a label"""
	if re.match("\([a-zA-Z_\.\$:][a-zA-Z0-9_\.\$:]*\)", instr) is not None:
		return instr[1:-1]
	return None


def parse_and_generate_symbols(lines):
	"""Parses lines and adds symbols to symbol table"""

	ram_addr = 16

	lines_no_labels = []
	
	for  line in lines:
		symbol = get_label_symbol(line)
	
		if symbol is not None and symbol not in symb:
			symb[symbol] = len(lines_no_labels)
		else:
			lines_no_labels.append(line)

	for line in lines_no_labels:
		symbol = get_variable_symbol(line)
		
		if symbol is not None and symbol not in symb:
			symb[symbol] = ram_addr
			ram_addr = ram_addr + 1
			
	return lines_no_labels

def split_instruction(instruction):
	instr = instruction.split('=')
	if len(instr) is 1: instr.insert(0, None);
	instr.extend(instr.pop().split(';'))
	if len(instr) is 2: instr.append(None);
	map(lambda x: x.strip() if x is not None else x, instr);
	return instr
        
def to_binary(*args):
	b = ""
	
	for arg in args:
	
		l = 0
		if type(arg) is tuple:
				l = int(arg[1])
				arg = arg[0]

		if type(arg) is int:
				arg = bin(arg)[2:]

		if type(arg) is not str:
				pass #throw error??
		else:
				b = b + "0" * (l-len(arg)) + arg
	return b
                        
def transform_to_machine_language(lines):
	instructions = []
	
	for line in lines:
			if len(line) > 0 and line[0] is '@':
				addr = line[1:]
				addr = symb[addr] if addr in symb else int(addr)
				instructions.append(to_binary("0", (addr, 15)))
			else:
				d, c, j = split_instruction(line)
				instructions.append(to_binary("111", comp[c], dest[d], jump[j]))
					
	return instructions

parsed_lines = parse_and_generate_symbols(prepared(sys.stdin))
sys.stdout.write("\n".join(transform_to_machine_language(parsed_lines)))
