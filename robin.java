// Die Toolbox als Bibliothek importiert.
import jtoolbox.*;

/**
 * Die Klasse Robin erzeugt im Konstruktor eine Leinwand und eine Eingabemoeglichkeit zur Schaetzung.
 * Ein Bild fuer das Rothkehlchen wird definiert.
 * Ein Timer wird initialisiert. 
 * 
 * Es gibt die Methoden
 * erzeugeSchwarm: Zufaellige Anzahl von Rothkehlchen wird an zufaellige Positionen gesetzt. Der Timer wird auf 10 Sekunden gesetzt. 
 * bericht: erzeugt eine Auswertung der Schaetzung
 * loescheBericht: setzt Berichtsausgaben auf unsichtbar
 * neuesSpiel: Menue um ein neues Spiel anzufangen
 * optionenSetzen: Die Spielschwierigkeit und das Bild auswaehlen
 * 
 * @author (Emilia Dolic) 
 * @version (Version: https://github.com/dolic72/rotkehlchen)
 */
public class robin implements ITuWas
{
    private     Behaelter       container;
    private     Bilddatei       bild;
    private     Bild[]          anzeigebild;
    private     StaticTools     s;
    private     Ausgabe         labelfeld;
    private     Eingabefeld     schaetzung;
    private     Ausgabe         anzahl;
    private     Ausgabe         ergebnis;
    private     Taktgeber       takt;
    private     AusgabePanel    countertext;
    private     Taste           taste;
    private     Schieberegler   level;
    private     Ausgabe         levelLeicht;
    private     Ausgabe         levelSchwer;
    private     Combobox        motiv;
    private     String[]        motive;
    int i;
    int menge;
    int schaetzwert;
    int daneben;
    int counter;


    /**
     * Erzeuge ein Spielfeld
     */
    public robin()
    {
        // Hier gewuenschte Bilder einfuegen:
        String[] motivBeschreibung = {"Rotkehlchen", "Katze"};
        motive = new String[motivBeschreibung.length];
        motive[0] = "robin-trans.png";
        motive[1] = "cat-trans.png";
       
        bild = new Bilddatei(motive[0], 40, 40);
        
        Zeichnung.setzeFenstergroesse(800, 800);
        container = new Behaelter(0, 200, 800, 600);
        container.setzeMitRand(true);

        
        labelfeld = new Ausgabe("Deine Schaetzung:", 50, 1, 200, 40);
        labelfeld.setzeAusrichtung(0);
        schaetzung = new Eingabefeld("", 50, 50, 100, 40);
        schaetzung.setzeLink(this, 0); 
        
        taste = new Taste("Neues Spiel", 300, 100, 200, 40);        
        level = new Schieberegler('H', 50, 100, 200, 30, 10, 120, 10);
        levelLeicht = new Ausgabe("Leicht", 30, 135, 100, 40);
        levelLeicht.setzeAusrichtung(0);
        levelSchwer = new Ausgabe("Schwer", 200, 135, 100, 40);
        levelSchwer.setzeAusrichtung(0);
        
        motiv = new Combobox(motivBeschreibung, 550, 100, 200, 40);
        
        counter = 0;
        schaetzwert = 0;
        daneben = 0;
        menge = s.gibZufall(70) + 30;
        
        optionenSetzen();
    }
 
    public void optionenSetzen(){
        taste.warteBisGedrueckt();
        bild.leseBildDatei(motive[motiv.leseAuswahlIndex()]);
        bild.einpassen(40, 40);

        if(level.hatSichGeaendert()){
            menge = s.gibZufall(level.leseIntWert()) + 20;
        }
        erzeugeSchwarm();
    }
    
    public void erzeugeSchwarm(){
        anzeigebild = new Bild[menge];
        
        for(int i=0; i<anzeigebild.length; i++)
        {
            anzeigebild[i] = new Bild(bild);
            anzeigebild[i].setzePosition(s.gibZufall(760), s.gibZufall(560) + 200);
        }
        
        counter = 10;
        countertext = new AusgabePanel(String.valueOf(counter), 1, 1, 40, 40);
        countertext.setzeHintergrundfarbe("rot");
        
        takt = new Taktgeber(this, 1);
        takt.setzteZeitZwischenAktionen(1000); 
        takt.mehrfach(counter);
    }
    
    public void tuWas(int ID){
        switch(ID)
        {
            case 0:
                schaetzwert = schaetzung.leseInteger(0);
                takt.stop();
                break;
            case 1:
                counter--;
                countertext.setzeAusgabetext(String.valueOf(counter));
        }
        if (!takt.laufend()){
            bericht();
            neuesSpiel();
        }
    }
    
    public void bericht(){
        daneben = Math.abs(menge - schaetzwert);
        anzahl = new Ausgabe("Anzahl der Tiere: "+menge, 300, 0, 300, 40);
        anzahl.setzeAusrichtung(0);
        ergebnis = new Ausgabe("Du lagst "+daneben+" Tiere daneben.", 300, 50, 400, 40);
        ergebnis.setzeAusrichtung(0);
    }
    
    public void loescheBericht(){
        anzahl.unsichtbarMachen();
        ergebnis.unsichtbarMachen();
        schaetzung.setzeAusgabetext("");
    }
    
    public void neuesSpiel(){
        D_Bestaetigung dlg = new D_Bestaetigung();
        dlg.setzeTitel("Wie geht es weiter?");
        dlg.setzeMeldungstext("Nochmal schaetzen?");
        dlg.icon_frage();
        dlg.typ_JaNeinAbbruch();
        dlg.zeigeMeldung();
        char erg = dlg.leseErgebnis();
        switch(erg)
        {
            case 'J':
                for(int i=0; i<anzeigebild.length; i++){
                    anzeigebild[i].entfernen();
                }
                loescheBericht();
                optionenSetzen();
                break;
            default:
                break;
        }    
    }
}


       