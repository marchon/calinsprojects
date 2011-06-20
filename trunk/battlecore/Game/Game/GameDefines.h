#pragma once

#define TOTAL_LOAD_STEPS	10

#define LOADING_MESSAGES	{							\
								"Initializing...",		\
								"Loading Map...",		\
								"Loading Hud...",		\
								"Prepare Camera...",	\
								"Load Vehicles...",		\
								"Load Explosions...",	\
								"Load Wepons...",		\
								"Create Players...",	\
								"Starting Game...",		\
								"...",		\
							}

#define UNLOADING_MESSAGES	{								\
								"Destroy Players...",		\
								"Unload Wepons...",			\
								"Unload Explosions...",		\
								"Unload Vehicle Info...",	\
								"Unload Vehicles...",		\
								"Shut Down Camera...",		\
								"Unload Hud...",			\
								"Unload Map...",			\
								"Finished...",				\
								"...",						\
							}

#define LOAD_MESSAGE_OFF_Y	100	

#define WAIT_AMOUNT_LOAD			400 //ms
#define WAIT_AMOUNT_UNLOAD			200 //ms