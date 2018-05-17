package conta;

import java.text.DecimalFormat;


/**
 * Convierte un numero a importe con letra
 * @author alberto
 *
 */

public class NumberToLetterConverter {

	public static String Num2Text(Double n, String Moneda){
        return NumberToLetterConverter.convertNumberToLetter(redondear(n),Moneda);
    }
	
	private static Double redondear( Double numero)
    {
        return Math.round(numero*Math.pow(10,2))/Math.pow(10,2);
    }
	
	private static final String[] UNIDADES = { "", "UN ", "DOS ", "TRES ",
			"CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ",
			"ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS",
			"DIECISIETE ", "DIECIOCHO ", "DIECINUEVE ", "VEINTE " };

	private static final String[] DECENAS = { "VEINTI", "TREINTA ", "CUARENTA ",
			"CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ",
			"CIEN " };

	private static final String[] CENTENAS = { "CIENTO ", "DOSCIENTOS ",
			"TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ",
			"SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS " };
	/**
	 * Convierte a letras un numero de la forma $123,456.32 (StoreMath)
	 * <p>
	 * Creation date 5/06/2006 - 10:20:52 AM
	 *
	 * @param number
	 *            Numero en representacion texto
	 * @return Numero en letras
	 * @since 1.0
	 */
//	public static String convertNumberToLetter(String number) {
//		return convertNumberToLetter(StoreMath.parseString(number));
//	}

	/**
	 * Convierte un numero en representacion numerica a uno en representacion de
	 * texto. El numero es valido si esta entre 0 y 999'999.999
	 * <p>
	 * Creation date 3/05/2006 - 05:37:47 PM
	 *
	 * @param number
	 *            Numero a convertir
	 * @return Numero convertido a texto
	 * @throws NumberFormatException
	 *             Si el numero esta fuera del rango
	 * @since 1.0
	 */
	public static String convertNumberToLetter(Double number, String Moneda)
			throws NumberFormatException {
		String converted = new String();

		// Validamos que sea un numero legal
		double doubleNumber = number;
		if (doubleNumber > 999999999)
			throw new NumberFormatException(
					"El numero es mayor de 999'999.999, "
							+ "no es posible convertirlo");

		DecimalFormat df = new DecimalFormat("############.00");
		String splitNumber[] = df.format(doubleNumber).replace('.', '#')
				.split("#");

		
		if (splitNumber[0].startsWith("-")){
			splitNumber[0]=splitNumber[0].substring(1);
			converted="MENOS ";
		}
			
		
		
		// Descompone el trio de millones - ¡SGT!
		int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
				8))
				+ String.valueOf(getDigitAt(splitNumber[0], 7))
				+ String.valueOf(getDigitAt(splitNumber[0], 6)));
		if (millon == 1)
			converted += "UN MILLON ";
		if (millon > 1)
			converted += convertNumber(String.valueOf(millon)) + "MILLONES ";

		// Descompone el trio de miles - ¡SGT!
		int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
				5))
				+ String.valueOf(getDigitAt(splitNumber[0], 4))
				+ String.valueOf(getDigitAt(splitNumber[0], 3)));
		if (miles == 1)
			converted += "MIL ";
		if (miles > 1)
			converted += convertNumber(String.valueOf(miles)) + "MIL ";

		// Descompone el ultimo trio de unidades - ¡SGT!
		int cientos = Integer.parseInt(String.valueOf(getDigitAt(
				splitNumber[0], 2))
				+ String.valueOf(getDigitAt(splitNumber[0], 1))
				+ String.valueOf(getDigitAt(splitNumber[0], 0)));
		if (cientos == 1)
			converted += "UN";

		if (millon + miles + cientos == 0)
			converted += "CERO";
		if (cientos > 1)
			converted += convertNumber(String.valueOf(cientos));

		converted += " "+(Moneda.equals("MXN")?"PESOS":Moneda.toUpperCase());

//		// Descompone los centavos - Camilo
                String sCent =  splitNumber[1];
//                int centavos = Integer.parseInt(String.valueOf(getDigitAt(
//				splitNumber[1], 2))
//				+ String.valueOf(getDigitAt(splitNumber[1], 1))
//				+ String.valueOf(getDigitAt(splitNumber[1], 0)));
//		if (centavos == 1)
//			converted += " CON UN CENTAVO";
//		if (centavos > 1)
//			converted += " CON " + convertNumber(String.valueOf(centavos))
//					+ "CENTAVOS";
                if (sCent.length() >1)
                    converted+= " "+ sCent + "/100 ";
                else
                    converted+= " "+ sCent + "0/100 ";

                if (Moneda.equals("MXN"))
                	converted+="M.N.";
                else
                    converted+=Moneda;
                        
		return converted;
	}

	/**
	 * Convierte los trios de numeros que componen las unidades, las decenas y
	 * las centenas del numero.
	 * <p>
	 * Creation date 3/05/2006 - 05:33:40 PM
	 *
	 * @param number
	 *            Numero a convetir en digitos
	 * @return Numero convertido en letras
	 * @since 1.0
	 */
	private static String convertNumber(String number) {
		if (number.length() > 3)
			throw new NumberFormatException(
					"La longitud maxima debe ser 3 digitos");

		String output = new String();
		if (getDigitAt(number, 2) != 0)
			output = CENTENAS[getDigitAt(number, 2) - 1];

		int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1))
				+ String.valueOf(getDigitAt(number, 0)));

		if (k <= 20)
			output += UNIDADES[k];
		else {
			if (k > 30 && getDigitAt(number, 0) != 0)
				output += DECENAS[getDigitAt(number, 1) - 2] + "Y "
						+ UNIDADES[getDigitAt(number, 0)];
			else
				output += DECENAS[getDigitAt(number, 1) - 2]
						+ UNIDADES[getDigitAt(number, 0)];
		}

		// Caso especial con el 100
		if (getDigitAt(number, 2) == 1 && k == 0)
			output = "CIEN";

		return output;
	}

	/**
	 * Retorna el digito numerico en la posicion indicada de derecha a izquierda
	 * <p>
	 * Creation date 3/05/2006 - 05:26:03 PM
	 *
	 * @param origin
	 *            Cadena en la cual se busca el digito
	 * @param position
	 *            Posicion de derecha a izquierda a retornar
	 * @return Digito ubicado en la posicion indicada
	 * @since 1.0
	 */
	private static int getDigitAt(String origin, int position) {
		if (origin.length() > position && position >= 0)
			return origin.charAt(origin.length() - position - 1) - 48;
		return 0;
	}
}

