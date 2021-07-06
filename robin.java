    // Die Toolbox als Bibliothek importiert.
import jtoolbox.*;

/**
 * Beschreiben Sie hier die Klasse robin.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
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
    private     D_Bestaetigung  dlg;
    int i;
    int menge;
    int schaetzwert;
    int daneben;
    int counter;


    /**
     * Konstruktor für Objekte der Klasse robin
     */
    public robin()
    {
        bild = new Bilddatei("robin-trans.png", 40, 40);
        
        Zeichnung.setzeFenstergroesse(800, 800);
        container = new Behaelter(0, 200, 800, 600);
        container.setzeMitRand(true);

        
        labelfeld = new Ausgabe("Deine Schaetzung:", 50, 1, 200, 40);
        schaetzung = new Eingabefeld("", 50, 50, 100, 40);
        schaetzung.setzeLink(this, 0); 
        
        counter = 0;
        schaetzwert = 0;
        
        erzeugeSchwarm();
    }
 
    
    public void erzeugeSchwarm(){
        menge = s.gibZufall(70) + 30;
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
            {
                schaetzwert = schaetzung.leseInteger(0);
                takt.stop();
                break;
            }
            case 1:
            {
                counter--;
                countertext.setzeAusgabetext(String.valueOf(counter));
            }
        }
        if (!takt.laufend()){
            bericht();
            neuesSpiel();
        }
    }
    
    public void bericht(){
        daneben = Math.abs(menge - schaetzwert);
        anzahl = new Ausgabe("Anzahl der Rotkehlchen: "+menge, 300, 0, 300, 40);
        anzahl.setzeAusrichtung(0);
        ergebnis = new Ausgabe("Du lagst "+daneben+" Rotkehlchen daneben.", 300, 50, 400, 40);
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
                erzeugeSchwarm();
                break;
            default:
                break;
        }
    }
    
    public boolean istFertig(){
        return counter < 1;
    }
}

       