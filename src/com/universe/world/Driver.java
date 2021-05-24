package com.universe.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Canberk
 * @version 1.0
 * @since 5-20-2021
 * @category Generic Algorithm
 * 
 *  Created by Canberk on 20/05/2021
 * */

public class Driver
{	
	private static final int PopulasyonBoyutu = 100;
	
	//Dize içerisinde ki tüm harfler bir gen olarak kabul edilir. Uygulanabilirlik açısından array zor 
	//olduğundan dolayı string tercih edildi..
	private static final String Genler = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890, .-;:_!\"#%&/()=?@${[]}";
	private static String hedef;
	
	public static void main(String[] args)
	{
	int Jenerasyon = 0;
		
    // Hedefi belirle
	System.out.println("Lütfen hedef değeri giriniz: ");
	hedef = new Scanner(System.in).nextLine(); 
	
	//Popülasyonu/Aday çözümleri oluştur
	ArrayList<Birey> populasyon = new ArrayList<Birey>();
	//Popülasyonu/aday çözümleri doldur
	for(int i = 0; i < PopulasyonBoyutu; i++)
	populasyon.add(new Birey(Kromozom_Yarat(),hedef));
	
	System.out.println("---------------- Arama hedefi Başlatıldı! -------------------------------------");
	System.out.println("Hedef değer: " + hedef + " olarak belirlendi. Gen sayısı: " + hedef.length());
	
	//Döngüye başla
	while(populasyon.get(0).Uygunluk != 0)
       {
		populasyon.sort(new PopSirala());
		
		//Genetik algoritmaların doğadan esinlenen "Doğal seçilim" sebebiyle popülasyonun (keyfi olarak)yüzde 10'u hayatta
		//kalır
		ArrayList<Birey> Elitler = new ArrayList<Birey>();
		
		//Elitleri bireyleri seç, Doğal seçilim sebebiyle bu bireylerin hayatta kalma ihtimali daha büyüktür ve bunları mutasyona uğrat
		for(int i = 0; i < (PopulasyonBoyutu*10)/100; i++) 
		{
			Elitler.add(populasyon.get(i));
			Elitler.get(i).Mutasyon();
		}
		
		for(int i = (PopulasyonBoyutu*10)/100; i < populasyon.size()-1; i++)
			populasyon.remove(i);
		
		//Elitleri bireyleri çaprazla (Algoritmanın olabildiğince çok optimize olabilmesi için tüm popülasyon yerine sadece
		//                             elitlerin çaprazlanmasına izin verilmektedir)
		Birey YeniBirey = Elitler.get(0).Caprazla(Elitler.get(new Random().nextInt(Elitler.size())), Genler);
		System.out.println();
		System.out.println("Yeni doğan birey: " + YeniBirey.toString());
		System.out.println();
		
		//Yeni bireyi popülasyona ekle. (Eğer uygunluğu düşükse zaten döngü başında elitlere katılacaktır)
		populasyon.add(YeniBirey);
		
		populasyon.get(0).UygunlukHesapla();
		
		System.out.println(Jenerasyon + ".Jenerasyon-> " + Elitler.get(0).toString());
		System.out.println(Elitler.toString());
		Jenerasyon++;
		
		
       }
	
	System.out.println("---------------- Arama hedefi sonlandırıldı! -------------------------------------");
	System.out.println("Hedef değere " + (Jenerasyon - 1) + ". jenerasyonda ulaşıldı...");
	
	}
	
	private static String Kromozom_Yarat()
	{
		String Gen = " ";
		
		for(int i = 0; i < hedef.length()-1; i++) { 
			Gen += Genler.charAt(new Random().nextInt(Genler.length()));
		}
		return Gen;
	}
	
}

class Birey
{
	//Kromozom: Hedef değere karşın çözüm önerisi, kromozomun her harfi bir gen olarak sayılır
	public String Kromozom;
	//Birey kromozomunun hedef değere uzaklığını belirlemek amacıyla kullanılır
	public int Uygunluk;
	//Hedef değeri işaretleyen bir referans
	private String Hedef;
	
	public Birey(String Kromozom, String Hedef)
	{
		this.Kromozom = Kromozom;
		this.Hedef = Hedef;
		Uygunluk = UygunlukHesapla();
	}
	
	public Birey Caprazla(Birey Birey2, String Gen)
	{ // 
		String dol = "";
		
		for(int i = 0; i < this.Kromozom.length(); i++)
		{
			float rand = new Random().nextFloat();
			if(rand < 0.45)
			dol += Kromozom.charAt(i);
			else if(rand > 0.45 && rand < 0.90)
			dol += Birey2.Kromozom.charAt(i);
			else
			dol += Gen.charAt(new Random().nextInt(Gen.length()));
			
		}
		
		return new Birey(dol, Hedef);
	}

	public void Mutasyon()
	{
		float rand = new Random().nextFloat();
		
		
	}
	
	public int UygunlukHesapla()

	{
		int uygunluk = 0;
		
		for(int i = 0; i < Hedef.length();i++)
		{
			char[] Hharf = Hedef.toCharArray();
			char[] Kharf = Kromozom.toCharArray();
			
			if(Kharf[i] != Hharf[i]) uygunluk++;
		}
		
		this.Uygunluk = uygunluk;
		
		return uygunluk;
	}
	
	@Override
	public String toString()
	{
		return "Kromozom: " + Kromozom + "     Uygunluk: " + Uygunluk;
	}
}

class PopSirala implements Comparator<Birey>
{

	@Override
	public int compare(Birey arg0, Birey arg1) {
		Birey b1 = (Birey)arg0;
		Birey b2 = (Birey)arg1;
		
		if(b1.Uygunluk == b2.Uygunluk)return 0;
		else if(b1.Uygunluk > b2.Uygunluk) return 1;
		else return -1;
	}
}