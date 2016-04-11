package Instr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Bond {
	public enum CouponFreq {
		ANNUAL, SEMI_ANNUAL, QTRLY, MONTHLY
	}
	static Calendar cal = Calendar.getInstance();
	static double[] T = { 1 / 12, 3 / 12, 6 / 12, 1, 2, 3, 5, 7, 10, 20, 30 };
	static double[] Z = { 0.20/100, 0.23/100, 0.34/100, 0.54/100, 0.70/100, 0.84/100, 1.16/100, 1.47/100, 1.72/100, 2.13/100, 2.55/100 };

	public static double[] price(double[] terms, double[] rates, double coupon, CouponFreq cpnfreq, Date maturity,
			double faceValue, Date today) {

		double result[] = new double[2];
		double pv = 0;
		int cpndays = 0;
		switch (cpnfreq) {
		case ANNUAL:
			
			cpndays = 365;
			break;
		case SEMI_ANNUAL:
			cpndays = 180;
			break;
		case QTRLY:
			cpndays = 90;
			break;
		case MONTHLY:
			cpndays = 30;
			break;
		default:
			result[0] = result[1] = -1;
			return result;

		}

		
		boolean atmaturity=true;
		int regular_cpn_days=cpndays;
		for (Date todate = maturity;; todate = addDays(todate, -cpndays)) {
			Date fromdate= addDays( todate, -cpndays);
			double from_yrs = diffYear(fromdate, today);
			double to_yrs = diffYear(todate, today);
			if (from_yrs > 0) {
				double rate = get_forward_rate( from_yrs, to_yrs, terms, rates );
				pv = (coupon*faceValue*cpndays/365.25 + ( atmaturity ? faceValue : 0  ) + pv ) / ( 1 + rate*cpndays/365.25 );
			} else { 
				// accrued calc fromdate to today
				//pv += coupon * faceValue* (-yrs) / (cpndays / 365.25);
				// today to todate
				double rate = get_forward_rate( 0, to_yrs, terms, rates );
				cpndays = diffDays(todate,today);
				pv = (coupon*faceValue*cpndays/365.25 + ( atmaturity ? faceValue : 0  ) + pv ) / ( 1 + rate*cpndays/365.25 );
				break;
			}
			atmaturity = false;
		}
		result[0]= pv;
		result[1] = coupon * faceValue/pv ; // annual coupon
		
		return result;
	}
	
	
	public static double ytm( double pv, double seed_rate, double coupon, CouponFreq cpnfreq, Date maturity, double faceValue, Date today) {
		double rate = seed_rate;
		int count = 0;
		double[] terms = new double[1];
		double[] rates = new double[1];
		terms[0]=0;
		rates[0] = seed_rate;
		while( count < 1000) {
			count++;
			double[]  result = price(terms, rates, coupon, cpnfreq, maturity, faceValue, today);
			//System.out.println("PV1=" + pv1 + " Pv=" + pv + " rate = "+ rate);
			double diff = Math.abs( result[0] - pv);
			if ( diff < 0.01) {
				return rates[0];
			}
			
			rates[0] += 0.1*( result[0] - pv)/ result[0];
			
		}
		return 0;
	}
	
	

	public static Date addDays(Date date, int days) {
		cal.setTime(date);
		cal.add(Calendar.DATE, days); 
		return cal.getTime();
	}

	public static Date addMonths(Date date, int months) {
		cal.setTime(date);
		cal.add(Calendar.MONTH, months); 
		return cal.getTime();
	}
	
	public static Date addYears(Date date, int years) {
		cal.setTime(date);
		cal.add(Calendar.YEAR, years); 
		return cal.getTime();
	}

	public static int diffDays(Date date1, Date date2) {
		double t = date1.getTime() - date2.getTime();
		return (int) Math.round(t / (1000 * 60 * 60 * 24));
	}

	public static double diffYear(Date date1, Date date2) {
		double t = date1.getTime() - date2.getTime();
		return (t / (1000 * 60 * 60 * 24 * 365.25));
	}

	public static double interp(double yrs, double[] terms, double[] rates) {
		if (yrs <= terms[0])
			return rates[0];
		if (yrs >= terms[terms.length - 1])
			return rates[rates.length - 1];

		for (int i = 0; i < terms.length - 1; i++) {
			if (terms[i] <= yrs && yrs <= terms[i + 1]) {
				return rates[i] + (yrs - terms[i]) * (rates[i + 1] - rates[i]) / (terms[i + 1] - terms[i]);
			}
		}
		return 0; // should nevere come here
	}
	
	public static double get_forward_rate( double t1, double t2, double[] terms, double[] rates )
	{
		double r1 = interp( t1, terms, rates);
		double r2 = interp( t2, terms, rates);
		return (r2*t2 - r1*t1)/( t2-t1);
	}
	
	
	public static void main(String[] args) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String maturitydate_str = "2046/05/15";
		double coupon = 0.05;
		CouponFreq cpnfreq = CouponFreq.SEMI_ANNUAL;
		double faceValue = 100;
		Date today = new Date();
		//for ( int i =0; i< Z.length; i++) Z[i]=0.05;
		Date maturity;
		//try {
			Date maturity1 = addYears( today, 15 ); //formatter.parse(maturitydate_str);
			double[] res = price(T, Z, coupon, cpnfreq, maturity1, faceValue, today) ;
			System.out.println( res[0] + " " + res[1] );
			double y =  ytm( res[0], res[1],  coupon,  cpnfreq,  maturity1,  faceValue, today) ;
			System.out.println("YTM=" + y );
			Date maturity2 = addYears( today, 30 ); //formatter.parse(maturitydate_str);
			res = price(T, Z, coupon, cpnfreq, maturity2, faceValue, today) ;
			System.out.println(res[0] + " " + res[1] );
			y =  ytm( res[0], res[1],  coupon,  cpnfreq,  maturity2,  faceValue, today) ;
			System.out.println("YTM=" + y );
			
			
		/*} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

	}
	
}



