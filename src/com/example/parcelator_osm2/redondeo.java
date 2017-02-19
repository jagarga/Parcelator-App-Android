package com.example.parcelator_osm2;

public class redondeo {
	
	public static double redondea(double num, int numdec)
	{
	double valor = 0;
	valor = num;
	
	if (numdec==1) {
		numdec=10;
	}
	
	if (numdec==2) {
		numdec=100;
	}
	
	if (numdec==3) {
		numdec=1000;
	}
	
	if (numdec==4) {
		numdec=10000;
	}
	
	if (numdec==5) {
		numdec=100000;
	}
	
	if (numdec==6) {
		numdec=1000000;
	}
	
	if (numdec==7) {
		numdec=10000000;
	}
	
	if (numdec==8) {
		numdec=100000000;
	}
	
	if (numdec==9) {
		numdec=1000000000;
	}

	valor = valor*numdec;
	valor = java.lang.Math.round(valor);
	valor = valor/numdec;

	return valor;

	}

}