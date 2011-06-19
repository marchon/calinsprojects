#include <graphics.h>
#include <conio.h>
#include <stdlib.h>
#include <stdio.h>
#include <dos.h>
#include <time.h>
#include <math.h>
#include <ctype.h>
#include <string.h>

#define MinX 0
#define MinY 0
#define MINX 1
#define MINY 1

#define Up 0x48
#define Down 0x50
#define Right 0x4d
#define Left 0x4b
#define Sparge 0x20
#define Pauza 0x4f

#define Esc 0x1b
#define Enter 0xd
#define Backspace 0x8

#define DA 'd'
#define NU 'n'
#define RESTARTARE 'r'
#define IESIRE 'i'

#define NIMIC 0
#define CARAMIDA 1
#define BORDURA -1
#define CIREASA 2
#define EROU 3
#define DUSMANU 4

#define PRIMUL_NIVEL 1
#define ULTIMUL_NIVEL 10
#define NR_VIETI 5

#define G_MICA 1
#define G_MARE 2

/*			Variabile de trebuinta				*/
int MaxX, MaxY;

void *caramida;
void *gud_gai[5][3];
void *bed_gai;
void *cireasa;

struct
      {
	int x;
	int y;
      } pos_dusman[8];

class game
     {
      public :
	  int viata;
	  int niv;
	  int cirese;
	  int sp;
	  char buf[4];
	  int x_afis;
	  int y_afis;
	  void afis_viata( int color );
	  void afis_niv( int color );
	  void afis_cirese( int color );
	  void afis_sp( int color );
      private:
	  void replace( int x, int color );
     } joc;

int BIN[50][50];
int POZ, X, Y;
int nr_dusmani;
double DIST_MAX;
int Salt;
int cr;
int l, h;

static char password[] = "ciresarii";

/*                           Prototipul functiilor			*/
void init( void );
void imagini( void );
void instructiuni( void );
void promo( void );
void final( void );
void bordare( void );
void gen_lab( void );
void iesire( void );
void resetare( void );
int parola( void );

int sp_misc( int x, int y );
int loc_misc( int x, int y );
int alaturi( int x, int y );
void sparge_caramida( int x, int y );
void miscare_dusman( int a, int b , int i );
double distanta( int x, int y );
void misc_aleatorie( int a, int b, int i );
void hatz(int m, int n, int o, int p);
int ucid (int x, int y, int i);
void meniu( char msg[] );

/*	          Functia care initializeaza modul grafic                */
void init( void )
 {

  int Gdr, Gmd, ErrCd;

  Gdr = DETECT;
  initgraph( &Gdr, &Gmd, "" );
  ErrCd = graphresult();
  if( ErrCd != grOk ){
    printf(" EROARE: %s\n", grapherrormsg( ErrCd ) );
    exit( EXIT_FAILURE );
  }

  MaxX = getmaxx() + 1;
  MaxY = getmaxy() + 1;

  settextstyle( DEFAULT_FONT, HORIZ_DIR, 1 );

  h = textheight( "V" );
  joc.y_afis = (MaxY - 10) - (h / 2);

#define MAXX MaxX/20 - 2
#define MAXY MaxY/20 - 3

}

/*				Functia MAIN				*/
void main( void )
{
 int i;
 int m;
 int c;
 double interv;
 clock_t t1, t2;

 init();

 setbkcolor( BLACK );
 imagini();
 meniu( "Apasa orice tasta pentru a incepe jocul..." );
 instructiuni();
 promo();

 JOC:

 joc.viata = NR_VIETI;
 joc.niv = PRIMUL_NIVEL;

 Repeat :

 joc.sp = 1;
 cr = 0;
 Salt = 0;

 interv = 0.332222 - 0.032222 * joc.niv;
 DIST_MAX = 3.051 * joc.niv + 6.103;

 bordare();
 gen_lab();

 joc.afis_viata( WHITE );
 joc.afis_niv( WHITE );
 joc.afis_cirese( WHITE );
 joc.afis_sp( WHITE );

 X = MINX;
 Y = MINY;
 BIN[X][Y] = EROU;
 POZ = 2;
 m = 1;
 t1 = clock();

 setcolor( BLACK );

 Again:

  putimage( X * 20, Y * 20, gud_gai[POZ][G_MICA], XOR_PUT );

  while( !kbhit() )
	    {
	     for( i = 1; i <= nr_dusmani; i++ )
		if( ucid( pos_dusman[i].x, pos_dusman[i].y, i ) )
			{
			 joc.afis_viata( BLACK );
			 joc.afis_sp( BLACK );
			 joc.viata--;
			 joc.sp = 0;
			 if ( !joc.viata )
				  {
				   meniu( "Ai murit!     R - Reincepe jocul  I - Iesi" );
				   resetare();
				   goto JOC;
				  }
			 joc.afis_viata( WHITE );
			 joc.afis_sp( WHITE );
			}
	     t2 = clock();
	     if( ((t2 - t1) / CLK_TCK) >= interv )
	      {
	       miscare_dusman( pos_dusman[m].x, pos_dusman[m].y, m );
	       m++;
	       if( m == nr_dusmani + 1) m = 1;
	       t1 = clock();
	      }
	    }

  c = getch();

  switch( c )
   {
    case Esc : iesire(); break;
    case Enter : if(parola()) goto Repeat; break;
    case Sparge : if( joc.sp )
		    {
		     if( POZ == 1 ) sparge_caramida( X, Y + 1 );
		     if( POZ == 2 ) sparge_caramida( X + 1, Y );
		     if( POZ == 3 ) sparge_caramida( X, Y - 1 );
		     if( POZ == 4 ) sparge_caramida( X - 1, Y );
		     joc.afis_sp( BLACK );
		     joc.sp--;
		     joc.afis_sp( WHITE );
		    }
		    break;
    case Pauza : {
		  PAUZA:
		  do {} while( !kbhit() );
		  c = getch();
		  if ( c != Pauza ) goto PAUZA;
		 }
		 break;
   }

  putimage( X * 20, Y * 20, gud_gai[POZ][G_MICA], XOR_PUT );

  switch( c )
   {
    case Up : {
	       if ( (POZ == 3) && (Y > MINY) )
				    {
				     BIN[X][Y] = NIMIC;
				     Y -= alaturi( X, Y - 1 );
				     BIN[X][Y] = EROU;
				    }
	       else POZ = 3;
	      }
	      break;

    case Down : {
		 if ( (POZ == 1) && (Y < MAXY) )
				    {
				     BIN[X][Y] = NIMIC;
				     Y += alaturi( X, Y + 1 );
				     BIN[X][Y] = EROU;
				    }
		 else POZ = 1;
		}
		break;

    case Right : {
		  if ( (POZ == 2) && (X < MAXX) )
				    {
				     BIN[X][Y] = NIMIC;
				     X += alaturi( X + 1, Y );
				     BIN[X][Y] = EROU;
				    }
		  else POZ = 2;
		 }
		 break;

    case Left : {
		 if ( (POZ == 4) && (X > MINX) )
				    {
				     BIN[X][Y] = NIMIC;
				     X -= alaturi( X - 1, Y );
				     BIN[X][Y] = EROU;
				    }
		 else POZ = 4;
		}
		break;

   }
 if ( Salt )
	    {
	     if( joc.niv == ULTIMUL_NIVEL + 1 )
		   {
		    meniu( "Felicitari! Ai terminat jocul!     R - Reincepe jocul  I - Iesi" );
		    resetare();
		    goto JOC;
		   }
	     cleardevice();
	     goto Repeat;
	    }
 fflush(stdin);
 goto Again;

}

/*			     Corpul functiilor 				*/
void imagini( void )
{
 int i;
 int poly[8];
 unsigned int size;

 int raza = 9, raz = 7, x, y;
 int stengl, endengl;

 size = imagesize( MinX, MinY, MinX + 19, MinY + 19 );

 /*                    Caramida                      */

 poly[0] = MinX;
 poly[1] = MinY;

 poly[2] = MinX + 19;
 poly[3] = MinY ;

 poly[4] = MinX + 19;
 poly[5] = MinY + 19;

 poly[6] = MinX;
 poly[7] = MinY + 19;

 setfillstyle( SOLID_FILL, BROWN );
 setcolor( WHITE );

 fillpoly( 4, poly);

 caramida = malloc( size );
 getimage( MinX, MinY, MinX + 19, MinY + 19, caramida);

 cleardevice();

 /*		       Personajul pozitiv                   */

 setlinestyle( SOLID_LINE, 1, NORM_WIDTH );
 setfillstyle( SOLID_FILL, RED );
 setcolor( RED );

 stengl = 0;
 endengl = 360;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 setcolor( WHITE );
 line( MinX + 10, MinY + 10, MinX + 10, MinY + 19 );
 putpixel( MinX + 5, MinY + 10, WHITE );
 putpixel( MinX + 5, MinY + 11, WHITE );
 putpixel( MinX + 6, MinY + 10, WHITE );
 putpixel( MinX + 6, MinY + 11, WHITE );

 gud_gai[1][1] = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, gud_gai[1][1] );

 cleardevice();

 setcolor( RED );

 stengl = 0;
 endengl = 360;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 setcolor( WHITE );
 line( MinX + 10, MinY + 10, MinX + 19, MinY + 10 );
 putpixel( MinX + 10, MinY + 5, WHITE );
 putpixel( MinX + 11, MinY + 5, WHITE );
 putpixel( MinX + 10, MinY + 6, WHITE );
 putpixel( MinX + 11, MinY + 6, WHITE );

 gud_gai[2][1] = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, gud_gai[2][1] );

 cleardevice();

 setcolor( RED );

 stengl = 0;
 endengl = 360;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 setcolor( WHITE );
 line( MinX + 10, MinY + 10, MinX + 10, MinY + 1 );
 putpixel( MinX + 5, MinY + 9, WHITE );
 putpixel( MinX + 5, MinY + 10, WHITE );
 putpixel( MinX + 6, MinY + 9, WHITE );
 putpixel( MinX + 6, MinY + 10, WHITE );

 gud_gai[3][1] = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, gud_gai[3][1] );

 cleardevice();

 setcolor( RED );

 stengl = 0;
 endengl = 360;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 setcolor( WHITE );
 line( MinX + 10, MinY + 10, MinX + 1, MinY + 10 );
 putpixel( MinX + 9, MinY + 5, WHITE );
 putpixel( MinX + 10, MinY + 5, WHITE );
 putpixel( MinX + 9, MinY + 6, WHITE );
 putpixel( MinX + 10, MinY + 6, WHITE );

 gud_gai[4][1] = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, gud_gai[4][1] );

 cleardevice();

 setcolor( RED );

 stengl = 0;
 endengl = 230;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 stengl = 310;
 endengl = 360;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 putpixel( MinX + 5, MinY + 10, WHITE );
 putpixel( MinX + 5, MinY + 11, WHITE );
 putpixel( MinX + 6, MinY + 10, WHITE );
 putpixel( MinX + 6, MinY + 11, WHITE );

 gud_gai[1][2] = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, gud_gai[1][2] );

 cleardevice();

 stengl = 40;
 endengl = 320;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 putpixel( MinX + 10, MinY + 5, WHITE );
 putpixel( MinX + 11, MinY + 5, WHITE );
 putpixel( MinX + 10, MinY + 6, WHITE );
 putpixel( MinX + 11, MinY + 6, WHITE );

 gud_gai[2][2] = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, gud_gai[2][2] );

 cleardevice();

 stengl = 0;
 endengl = 50;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 stengl = 130;
 endengl = 360;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 putpixel( MinX + 5, MinY + 9, WHITE );
 putpixel( MinX + 5, MinY + 10, WHITE );
 putpixel( MinX + 6, MinY + 9, WHITE );
 putpixel( MinX + 6, MinY + 10, WHITE );

 gud_gai[3][2] = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, gud_gai[3][2] );

 cleardevice();

 stengl = 0;
 endengl = 140;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 stengl = 220;
 endengl = 360;
 sector( MinX + 10, MinY + 10, stengl, endengl, raza, raza );
 putpixel( MinX + 9, MinY + 5, WHITE );
 putpixel( MinX + 10, MinY + 5, WHITE );
 putpixel( MinX + 9, MinY + 6, WHITE );
 putpixel( MinX + 10, MinY + 6, WHITE );

 gud_gai[4][2] = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, gud_gai[4][2] );

 cleardevice();

 /*			Personajul negativ			*/

 setfillstyle( SOLID_FILL, BLUE );
 setcolor( RED );
 fillellipse( MinX + 10, MinY + 8, raza, raz);
 x=2;
 y=13;
 line( x, y, x - 2, y + 4 );
 x=4;
 y=14;
 line( x, y, x - 2, y + 4 );
 x=6;
 y=15;
 line( x, y, x - 2, y + 4 );
 x=14;
 y=15;
 line( x, y, x + 2, y + 4 );
 x=16;
 y=14;
 line( x, y, x + 2, y + 4 );
 x=18;
 y=13;
 line( x, y, x + 2, y + 4 );
 putpixel( MinX + 6, MinY + 6, WHITE);
 putpixel( MinX + 14, MinY + 6, WHITE);
 setcolor( WHITE );
 line( MinX + 7, MinY + 11, MinX + 13, MinY + 11 );
 putpixel( MinX + 7, MinY + 12, WHITE);
 putpixel( MinX + 13, MinY + 12, WHITE);

 bed_gai = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, bed_gai );

 cleardevice();

 /*				Cireasa				*/

 setfillstyle( SOLID_FILL, LIGHTRED );
 setcolor( RED );

 raza = 5;
 fillellipse( MinX + 10, MinY + 10, raza, raza );

 setcolor( LIGHTGREEN );

 stengl = 120;
 endengl = 180;
 raza = 10;
 arc( MinX + 20, MinY + 7, stengl, endengl, raza );

 cireasa = malloc( size );
 getimage( MinX + 1, MinY + 1 , MinX + 19, MinY + 19, cireasa );

 cleardevice();

}

void instructiuni( void )
{
 settextstyle( DEFAULT_FONT, HORIZ_DIR, 1 );
 settextjustify( CENTER_TEXT, TOP_TEXT );

 int x = MaxX / 2;
 int y = 2.5 * MaxY / 4;
 int h = textheight( "A" ) + 2;

 char ch[100];

 outtextxy( x, y, "Instructiuni:" );
 y += 2 * h;
 outtextxy( x, y, "    Te afli intr-un labirint. Scopul tau este sa mananci toate ciresele pentru " );
 y += h;
 outtextxy( x, y, "a trece la urmatorul nivel, evitand intalnirea cu monstii. Jocul are 10 nivele " );
 y += h;
 outtextxy( x, y, "de dificultate, complexitatea labirintului si numarul cat si viteza monstrilor " );
 y += h;
 outtextxy( x, y, "crescand odata cu acestea. La fiecare 10 cirese mancate poti sparge o caramida " );
 y += h;
 outtextxy( x, y, "in caz de nevoie. Jocul se termina la epuizarea celor cinci vieti sau la termi-" );
 y += h;
 outtextxy( x, y, "narea ultimului nivel. Distractie placuta!                                     " );

 settextjustify( LEFT_TEXT, TOP_TEXT );
 x = MaxX / 6;

 y += 3 * h;
 sprintf( ch, "Controale:   %c - SUS                      %c - JOS", 24, 25 );
 outtextxy( x, y, ch );
 y += h;
 sprintf( ch, "             %c - DREAPTA                  %c - STANGA", 26, 27 );
 outtextxy( x, y, ch );
 y += 2 * h;
 sprintf( ch, "             Space - SPARGE CARAMIDA      Esc - MENIU" );
 outtextxy( x, y, ch );
 y += h;
 sprintf( ch, "             Enter - PAROLA               End - PAUZA" );
 outtextxy( x, y, ch );
}

void promo ( void )
{

 setcolor( LIGHTRED );
 settextstyle( TRIPLEX_FONT, HORIZ_DIR, 6 );
 settextjustify( CENTER_TEXT, CENTER_TEXT );
 outtextxy( MaxX / 2, MaxY / 12, "LABIRINT" );
 settextstyle( DEFAULT_FONT, HORIZ_DIR, 1 );
 settextjustify( LEFT_TEXT, TOP_TEXT );

 int x, y, raza, st, end;

 x = MaxX / 2 + 40;
 y = MaxY / 2 - 25;
 raza = 60;
 setcolor( RED );
 setfillstyle( SOLID_FILL, RED );
 fillellipse( x, y, raza, raza );
 setcolor( WHITE );
 setfillstyle( SOLID_FILL, WHITE );
 raza = 5;
 x -= 15;
 y -= 17;
 fillellipse( x, y, raza + 1, raza );
 x += 50;
 y -= 7;
 fillellipse( x, y, raza + 1, raza );
 setfillstyle( SOLID_FILL, BLACK );
 raza = 20;
 x -= 17;
 y += 45;
 fillellipse( x, y, 2 * raza , 1.25 * raza );

 x = (5 * MaxX) / 8 - 7;
 y = MaxY / 2;
 raza = 30;
 setcolor( RED );
 setfillstyle( SOLID_FILL, LIGHTRED );
 fillellipse( x, y, raza, raza );
 setcolor( LIGHTGREEN );
 raza = 60;
 st = 140;
 end = 180;
 x += 60;
 y -= 18;
 arc( x, y, st, end, raza );

 x = MaxX / 3;
 y = MaxY / 3;
 raza = 18;
 setcolor( RED );
 setfillstyle( SOLID_FILL, BLUE );
 fillellipse( x, y, raza + 4, raza );
 raza = 1;
 setcolor( WHITE );
 setfillstyle( SOLID_FILL, WHITE );
 x -= 7;
 y -= 5;
 fillellipse( x, y, raza, raza );
 x += 18;
 y -= 2;
 fillellipse( x, y, raza, raza );
 x -= 15;
 y += 13;
 line( x, y, x + 16, y - 2 );
 x += 2;
 y++;
 putpixel( x, y, WHITE );
 putpixel( x + 1, y, WHITE );
 putpixel( x, y + 1, WHITE );
 putpixel( x + 1, y + 1, WHITE );
 x += 11;
 y -= 2;
 putpixel( x, y, WHITE );
 putpixel( x + 1, y, WHITE );
 putpixel( x, y + 1, WHITE );
 putpixel( x + 1, y + 1, WHITE );
 setlinestyle( SOLID_LINE, 1, THICK_WIDTH );
 setcolor( RED );
 x = MaxX / 3 - 13;
 y = MaxY / 3 + 11;
 line( x, y, x - 5, y + 5 );
 x -= 5;
 y += 5;
 line( x, y, x - 1, y + 5 );
 x = MaxX / 3 - 9;
 y = MaxY / 3 + 14;
 line( x, y, x - 5, y + 5 );
 x -= 5;
 y += 5;
 line( x, y, x - 1, y + 5 );
 x = MaxX / 3 - 5;
 y = MaxY / 3 + 16;
 line( x, y, x - 5, y + 5 );
 x -= 5;
 y += 5;
 line( x, y, x - 1, y + 5 );
 x = MaxX / 3 + 16;
 y = MaxY / 3 + 12;
 line( x, y, x + 4, y + 4 );
 x += 4;
 y += 4;
 line( x, y, x + 1, y + 6 );
 x = MaxX / 3 + 13;
 y = MaxY / 3 + 15;
 line( x, y, x + 2, y + 2 );
 x += 2;
 y += 2;
 line( x, y, x + 1, y + 4 );
 x = MaxX / 3 + 10;
 y = MaxY / 3 + 17;
 line( x, y, x + 1, y + 1 );
 x += 1;
 y += 1;
 line( x, y, x , y + 1 );
 setlinestyle( SOLID_LINE, 1, NORM_WIDTH );

 getch();
 cleardevice();
}

void final( void )
{
 setcolor( WHITE );
 settextstyle( SMALL_FONT, HORIZ_DIR, 5 );
 settextjustify( CENTER_TEXT, CENTER_TEXT );
 outtextxy( 1.25 * MaxX / 2, 7 * MaxY / 8, "Programator, designer, etc., etc.: CALIN AVASILCAI" );

 free( caramida );
 free( cireasa );
 free( gud_gai );
 free( bed_gai );
}

void bordare( void )
{
 int i, k ,l;

 for( k = MINX; k <= MAXX; k++ )
     for( l = MINY; l <= MAXY; l++ )
	BIN[k][l] = NIMIC;
 for ( k = MINX - 1; k <= MAXX + 1; k++ )
     {
      BIN[k][MINY - 1] = BORDURA;
      BIN[k][MAXY + 1] = BORDURA;
     }
 for ( l = MINY - 1; l <= MAXY + 1; l++ )
     {
      BIN[MINX - 1][l] = BORDURA;
      BIN[MAXX + 1][l] = BORDURA;
     }

 for( i = MINX - 1; i <= MAXX + 1; i++ )
  {
   putimage( i * 20, MinY, caramida, XOR_PUT );
   putimage( i * 20, MaxY - 40, caramida, XOR_PUT);
  }
 for( i = MINY; i <= MAXY; i++ )
  {
   putimage( MinX, i * 20, caramida, XOR_PUT );
   putimage( MaxX - 20, i * 20, caramida, XOR_PUT);
  }
  meniu( "Viata:          Nivel:          Cirese Ramase:          Spargeri:  " );
}

void gen_lab( void )
{
 randomize();

 int x, y, k;
 joc.cirese = 0;

 for( x = MINX + 1; x <= MAXX; x++ )
       for( y = MINY; y <= MAXY; y++ )
	    {
		k = random( 13 - joc.niv );
		if( !k )
		     {
		      BIN[x][y] = CARAMIDA;
		      putimage( x * 20, y * 20, caramida, XOR_PUT );
		     }
		if( k == 1 )
		       {
			BIN[x][y] = CIREASA;
			joc.cirese++;
			putimage(x * 20, y * 20, cireasa, XOR_PUT );
		       }
	    }

 nr_dusmani = (joc.niv / 2) + 2;

 for( k = 1; k <= nr_dusmani ; k++ )
     {
      do{
	 x = 5 + random( MAXX - 10 + 1 );
	 y = 5 + random( MAXY - 10 + 1 );
	}while( !sp_misc( x, y ) );
      BIN[x][y] = DUSMANU;
      pos_dusman[k].x = x;
      pos_dusman[k].y = y;
      putimage( x * 20, y * 20, bed_gai, XOR_PUT );
     }

}

void iesire( void )
{
 int c;
 meniu( "Parasesti jocul?     D - Da  N - Nu" );
 Ciclu:
  c = getch();
  switch( tolower( c ) )
   {
    case DA : {
	       cleardevice();
	       meniu( "Apasa orice tasta pentru a parasi jocul..." );
	       final();
	       promo();
	       closegraph();
	       exit( EXIT_SUCCESS );
	      }
    case NU : {} ; break;
    default : goto Ciclu;
   }
 meniu( "Viata:          Nivel:          Cirese Ramase:          Spargeri:  " );
 joc.afis_viata( WHITE );
 joc.afis_niv( WHITE );
 joc.afis_cirese( WHITE );
 joc.afis_sp( WHITE );
}

void resetare( void )
{
 int c;
 Ciclu:
  c = getch();
  switch( tolower( c ) )
   {
    case IESIRE : {
		   cleardevice();
		   meniu( "Apasa orice tasta pentru a parasi jocul..." );
		   final();
		   promo();
		   closegraph();
		   exit( EXIT_SUCCESS );
		  }
    case RESTARTARE : {
		       if( joc.niv == ULTIMUL_NIVEL + 1 )
			{
			 static char msg[40];
			 sprintf( msg, "Parola este: %s. Apasa o tasta...", password );
			 meniu( msg );
			 getch();
			}
		       cleardevice();
		      }
		      break;
    default : goto Ciclu;
   }
}

int parola( void )
{
 int nrmax = strlen( password );
 char read_pass[20], read_lev[2], ch[1];
 int x, l;
 int c, m = 0;
 int i;

 meniu( "Introdu parola:                " );
 x = MaxX / 2;
 l = textwidth( "*" );

 Ciclu1:
  c = getch();
  switch( c )
	{
	 case Enter : {}; break;
	 case Backspace: if( m > 0 )
			     {
			      m--;
			      read_pass[m] = ' ';
			      x -= l;
			      setcolor( BLACK );
			      outtextxy( x, joc.y_afis, ch );
			      setcolor( WHITE );
			      ch[0] = read_pass[m-1];
			     }
			 goto Ciclu1;


	 default : if( m < nrmax )
			{
			 sprintf( ch, "%c", c );
			 read_pass[m] = ch[0];
			 outtextxy( x, joc.y_afis, ch );
			 m++;
			 x += l;
			}
		   goto Ciclu1;

	}

 if ( !stricmp( password, read_pass ) )
		       {
			meniu( "Introdu nivelul(1 - 10):" );
			x = MaxX / 2 + textwidth( "Inrodu nivelul(1 - 10):" ) / 2 + 5;
			nrmax = strlen( "10" );
			m = 0;
			Ciclu2:
			 c = getch();
			 switch( c )
			  {
			   case Enter : {}; break;
			   case Backspace: if( m > 0 )
						{
						 m--;
						 read_lev[m] = ' ';
						 x -= l;
						 setcolor( BLACK );
						 outtextxy( x, joc.y_afis, ch );
						 setcolor( WHITE );
						 ch[0] = read_lev[m-1];
						}
					   goto Ciclu2;


			   default : if( m < nrmax )
					{
					 sprintf( ch, "%c", c );
					 read_lev[m] = ch[0];
					 outtextxy( x, joc.y_afis, ch );
					 m++;
					 x += l;
					}
				     goto Ciclu2;

			  }
			if( atoi( read_lev ) >= PRIMUL_NIVEL && atoi( read_lev ) <= ULTIMUL_NIVEL )
				  {
				   joc.niv = atoi( read_lev );
				   cleardevice();
				   return 1;
				  }
		       }
 else
     {
      meniu( "Parola incorecta!!" );
      delay( 1250 );
     }
 setcolor( BLACK );
 meniu( "Viata:          Nivel:          Cirese Ramase:          Spargeri:  " );
 joc.afis_viata( WHITE );
 joc.afis_niv( WHITE );
 joc.afis_cirese( WHITE );
 joc.afis_sp( WHITE );
 return 0;

}

int sp_misc( int x, int y )
{
 if( BIN[x][y] != NIMIC ) return 0;

 if( BIN[x+1][y] == NIMIC ) return 1;
 if( BIN[x-1][y] == NIMIC ) return 1;
 if( BIN[x][y+1] == NIMIC ) return 1;
 if( BIN[x][y-1] == NIMIC ) return 1;

 return 0;
}

int loc_misc( int x, int y )
{
 if( BIN[x+1][y] == NIMIC ) return 1;
 if( BIN[x-1][y] == NIMIC ) return 1;
 if( BIN[x][y+1] == NIMIC ) return 1;
 if( BIN[x][y-1] == NIMIC ) return 1;

 return 0;
}

int alaturi( int x, int y )
{
 switch( BIN[x][y] )
       {
	case CARAMIDA : return 0;
	case CIREASA :
		  {
		   BIN[x][y] = NIMIC;
		   putimage( x * 20, y * 20, cireasa, XOR_PUT );
		   putimage( x * 20, y * 20, gud_gai[POZ][G_MARE], XOR_PUT );
		   sound( 100 );
		   delay( 100 );
		   nosound();
		   putimage( x * 20, y * 20, gud_gai[POZ][G_MARE], XOR_PUT );
		   cr++;
		   if(cr == 10)
		     {
		      joc.afis_sp( BLACK );
		      joc.sp++;
		      joc.afis_sp( WHITE );
		      cr = 0;
		     }
		   joc.afis_cirese( BLACK );
		   joc.cirese--;
		   joc.afis_cirese( WHITE );
		   if ( !joc.cirese )
		      {
		       joc.niv++;
		       Salt = 1;
		      }
		   return 1;
		  }
	case NIMIC : return 1;
       }
  return 0;
 }

void sparge_caramida( int x, int y )
{
 if ( BIN[x][y] == CARAMIDA )
    {
     setlinestyle( SOLID_LINE, 1, THICK_WIDTH );

     line( x * 20 + 15, y * 20 + 1, x * 20 + 5, y * 20 + 5 );
     delay(150);
     line( x * 20 + 5, y * 20 + 5, x * 20 + 15, y * 20 + 10 );
     delay(150);
     line( x * 20 + 15, y * 20 + 10, x * 20 + 5, y * 20 + 15 );
     delay(150);
     line( x * 20 + 5, y * 20 + 15, x * 20 + 15, y * 20 + 20 - 2);
     delay(150);

     setlinestyle( SOLID_LINE, 1, NORM_WIDTH );
     bar( x * 20, y * 20, x * 20 + 19, y * 20 + 19);
     BIN[x][y] = NIMIC;
    }

}

void miscare_dusman( int a, int b, int i )
{
 int k;
 double dist, d;

 dist = distanta( a, b );

 if( dist <= DIST_MAX )
   {
    k = 0;
    if( loc_misc( a, b ) )
     {
      if( BIN[a - 1][b] == NIMIC )
       {
	d = distanta( a - 1, b );
	if( d < dist )
	  {
	   dist = d;
	   k = 1;
	  }
       }
      if( BIN[a + 1][b] == NIMIC )
       {
	d = distanta( a + 1, b );
	if( d < dist )
	  {
	   dist = d;
	   k = 2;
	  }
       }
      if( BIN[a][b - 1] == NIMIC )
       {
	d = distanta( a, b - 1 );
	if( d < dist )
	  {
	   dist = d;
	   k = 3;
	  }
       }
      if( BIN[a][b + 1] == NIMIC )
       {
	d = distanta( a, b + 1 );
	if( d < dist )
	  {
	   dist = d;
	   k = 4;
	  }
       }
     if( k )
      {
       BIN[a][b] = NIMIC;
       putimage( a * 20, b * 20, bed_gai, XOR_PUT );
       switch( k )
	{
	 case 1 : a--; break;
	 case 2 : a++; break;
	 case 3 : b--; break;
	 case 4 : b++; break;
	}

       BIN[a][b] = DUSMANU;
       putimage( a * 20, b * 20, bed_gai, XOR_PUT );
       pos_dusman[i].x = a;
       pos_dusman[i].y = b;
      }
     else misc_aleatorie( a, b, i );
     }
   }
 else misc_aleatorie( a, b, i );
}

double distanta( int x, int y )
{
 double cat1_2, cat2_2;
 cat1_2 = abs( X - x ) * abs( X - x );
 cat2_2 = abs( Y - y ) * abs( Y - y );
 return  sqrt( cat1_2 + cat2_2 );
}

void misc_aleatorie ( int a, int b, int i )
{
 int k;
 int x, y;
 int stop;

 if( loc_misc( a, b ) )
 {
  stop = 0;
  while( !stop )
  {
   x = 0;
   y = 0;
   k = random( 2 );

   switch( k )
    {
     case 0 : {
	       x = 2 * random( 2 ) - 1;
		if( (BIN[a + x][b] == NIMIC) )
			    stop = 1;
	      }
	      break;

     case 1 : {
	       y = 2 * random( 2 ) - 1;
		if( BIN[a][b + y] == NIMIC )
			    stop = 1;
	      }
	      break;
    }
  }
  BIN[a][b] = NIMIC;
  putimage( a * 20, b * 20, bed_gai, XOR_PUT );
  a += x;
  b += y;
  BIN[a][b] = DUSMANU;
  putimage( a * 20, b * 20, bed_gai, XOR_PUT );
  pos_dusman[i].x = a;
  pos_dusman[i].y = b;
 }
}

void hatz(int m, int n, int o, int p)
{
 delay( 500 );
 putimage( m * 20, n * 20, gud_gai[POZ][G_MICA], XOR_PUT );
 delay( 500 );
 putimage( m * 20, n * 20, gud_gai[POZ][G_MICA], XOR_PUT );
 delay( 500 );
 putimage( m * 20, n * 20, gud_gai[POZ][G_MICA], XOR_PUT );
 delay( 500 );
 putimage( m * 20, n * 20, gud_gai[POZ][G_MICA], XOR_PUT );
 delay( 500 );
 putimage( m * 20, n * 20, gud_gai[POZ][G_MICA], XOR_PUT );
 BIN[o][p] = NIMIC;
 putimage( o * 20, p * 20, bed_gai, XOR_PUT );
 BIN[m][n] = DUSMANU;
 putimage( m * 20, n * 20, bed_gai, XOR_PUT );
 delay( 500 );

 X = MINX;
 if( BIN[MINX][MINY] == NIMIC ) Y = MINY;
 else do
	{
	  Y = 5 + random( MAXY - 5 + 1);
	} while( BIN[X][Y] != NIMIC );
 POZ = 2;
 BIN[X][Y] = EROU;
 putimage( X * 20, Y * 20, gud_gai[POZ][G_MICA], XOR_PUT);
}

int ucid ( int x, int y, int i )
 {
  if( BIN[x-1][y] == EROU )
	 {
	  hatz( x - 1, y, x, y);
	  pos_dusman[i].x --;
	  return 1;
	 }
  if( BIN[x+1][y] == EROU )
	 {
	  hatz( x + 1, y, x, y);
	  pos_dusman[i].x ++;
	  return 1;
	 }
  if( BIN[x][y-1] == EROU )
	 {
	  hatz( x, y - 1, x, y );
	  pos_dusman[i].y --;
	  return 1;
	 }
  if( BIN[x][y+1] == EROU )
	 {
	  hatz( x, y+1, x, y );
	  pos_dusman[i].y ++;
	  return 1;
	 }

  return 0;
 }

void meniu( char msg[] )
 {
  setfillstyle( SOLID_FILL, BLACK );
  bar( 0, MaxY - 20, MaxX - 1, MaxY - 1 );
  setcolor( WHITE );
  rectangle( 0, MaxY - 20, MaxX - 1, MaxY - 1 );
  l = textwidth( msg );
  joc.x_afis = (MaxX / 2) - (l / 2);
  outtextxy( joc.x_afis, joc.y_afis, msg );
 }

void game::replace( int x, int color)
 {
  switch( color )
   {
    case BLACK : outtextxy( x, y_afis, buf ); break;
    case WHITE : {
		  setcolor( WHITE );
		  outtextxy( x, y_afis, buf );
		  setcolor( BLACK );
		 }
		 break;
   }
 }

void game::afis_viata( int color )
 {
  gcvt( viata, 1, buf);
  int x;
  x = x_afis + textwidth( "Viata :  " );
  replace( x, color );
 }

void game::afis_niv( int color )
 {
  gcvt( niv, 2, buf );
  int x;
  x = x_afis + textwidth( "Viata:          Nivel:  " );
  replace( x, color );
 }

void game::afis_cirese( int color )
 {
  gcvt( cirese, 3, buf );
  int x;
  x = x_afis + textwidth( "Viata:          Nivel:          Cirese Ramase:  ");
  replace( x, color );
 }

void game::afis_sp( int color )
 {
  gcvt( sp, 3, buf );
  int x;
  x = x_afis + textwidth( "Viata:          Nivel:          Cirese Ramase:          Spargeri:  ");
  replace( x, color );
 }