import re

comp = {
}

dest = {
}

jump = {
}

symb = {
}

def prepared(lines):
	"""Eliminate white spaces and comments"""

	for line in lines:
		yield line

def get_variable_symbol(instr):
	"""Returns the variable name in an A-instruction, if the case"""
	return None

def get_label_symbol(instr):
	"""Returns the label name, if this is a label"""
	return None


def parse_and_generate_symbols(lines):
	"""Parses prepared lines and adds symbols if needed"""

	ram_addr = 16
	rom_addr = 0

	toremove = []

	for i, line in enumerate(lines):
		symbol = get_variable_symbol(line)
		
		if symbol is not None:
			if symbol not in symb:
				symb[symbol] = ram_addr
				ram_addr = ram_addr + 1
		else:
			symbol = get_label_symbol(line)
			if symbol is not None and symbol not in symb:
				symb[symbol] = rom_addr
				rom_addr = rom_addr + 1
				toremove.append(n)
					
	map(lines.pop, toremove)
	return lines

def split_instruction(instruction):
	instr = map(lambda s: s.strip(), re.split('=|;', a))
	while len(instr) < 3:
                instr.append(None)
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
			return None #throw error
		else:
			return "0" * (l-len(arg)) + arg
			
def transform_to_machine_language(lines):
	instructions = []

	for line in lines:
		if len(line) > 0 and line[0] is '@':
			addr = line[1:]
			addr = int(symb[addr] if addr in symb else addr)
			instructions.append(to_bynary("1", (addr, 15)))
		else:
			d, c, j = split_instruction(line)
			instructions.append(to_bynary("111", d, c, j))
			
	return intructions





parsed_lines = parse_and_generate_symbols(prepared(sys.stdin))
sys.stdout.write("\n".join(transform_to_machine_language(parsed_lines)))
