#include <string.h>

#define NICK_MAX_LEN					32

#define TALK_MSG_TYPE_BEGIN_NICK_LIST	"\n"
#define TALK_MSG_TYPE_END_NICK_LIST		"\n"
#define TALK_MSG_TYPE_BAD_NICKNAME		"\t"

#define TALK_MSG_VAL_LENGTH(str)		(strlen(str) + 1)
#define TALK_MSG_BOOL_IS_MSG(str, msg)	(!strcmp(str, msg))

//--

#define TALK_MSG_TYPE_NORMAL_MSG				'0'
#define TALK_MSG_TYPE_CLIENT_CONN_NOTIF			'+'
#define TALK_MSG_TYPE_CLIENT_DISC_NOTIF			'-'

#define TALK_MSG_VAL_MSG_TYPE(str)				(str[0])
#define TALK_MSG_VAL_MAKE_NORMAL_LEN(msg, nick)	(strlen(msg) + strlen(nick) + 4)
#define TALK_MSG_VAL_MAKE_NOTIF_LEN(msg)		(strlen(msg) + 2)

// |0|nick...|\n|msg...|\0|
#define TALK_MAKE_MSG_NORMAL_MSG(nick, msg, out)	\
	out[0] = TALK_MSG_TYPE_NORMAL_MSG;				\
	strcpy(out + 1, nick);							\
	strcat(out, "\n");								\
	strcat(out, msg)

#define TALK_GET_MSG_NORMAL_MSG_DEST_MSG(str, nick, msg)	\
		nick = str + 1;										\
		int __i = 2;										\
		while(str[__i++] != '\n');							\
		str[__i - 1] = '\0';								\
		msg = (str + __i)			

#define TALK_MAKE_MSG_CLIENT_CONN_NOTIF(nick, out)		\
	out[0] = TALK_MSG_TYPE_CLIENT_CONN_NOTIF;			\
	strcpy(out + 1, nick)

#define TALK_MAKE_MSG_CLIENT_DISC_NOTIF(nick, out)		\
	out[0] = TALK_MSG_TYPE_CLIENT_DISC_NOTIF;			\
	strcpy(out + 1, nick)

#define TALK_GET_MSG_CLIENT_CONN_NOTIF(str)	(str + 1)
#define TALK_GET_MSG_CLIENT_DISC_NOTIF(str)	(str + 1)




