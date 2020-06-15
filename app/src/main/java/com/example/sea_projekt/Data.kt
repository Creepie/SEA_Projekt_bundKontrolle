package com.example.sea_projekt

//Platz Object > Daten pro Ablageplatz
data class Platz(val id: Int,                                   //
                 val rinne: Int,                                //
                 val barcode: String,                           //Barcode der Bundablage falls vorhanden
                 val bezeichnung: String,                       //Bezeichnung der Bundablage
                 val bund: Bund)                                //Objekt des Bundes auf Ablage

//Bund Object > Daten pro Bund > Teil von Platz Objekt
data class Bund(val bundId: Int,                                //Bund ID
                val menr: String,                               //
                val untr: String,                               //
                var bundKontrolliert: Boolean,                  //Bund kontrolliert Ja/Nein
                val bundVerbucht: Boolean,                      //Bund verbucht Ja/Nein
                val bundGesperrt: Boolean,                      //Bund gesperrt Ja/Nein
                val folgeAst: String,                           //folge Arbeitsstufe (für Bundinfos)
                val baender: MutableList<Band>,                 //Liste an Bändern (Teilung eines Bundes möglich)
                val bundParameter: MutableList<Parameter>)      //Liste an Parameter (Teilung eines Bundes möglich)

//Band Object > Daten pro Band > Teilung pro Bund möglich > Teil von Bund Objekt
data class Band(val bandId: Int,                                //Band ID
                val bandauslID: Int,                            //
                val menr: String,                               //
                val untr: String,                               //
                var inspektionsdatensatz: MutableList<Fehler>,  //Liste an Fehlern auf Band (derzeit auf Bundplatz zu Testzwecken)
                val evParameter: MutableList<Parameter>,        //Liste an Parametern von dominierten Auftrag
                val istParameter: MutableList<Parameter>)       //Liste an Parametern

//Parameter Objekt > Teilung pro Bund möglich daher verschiedene Parameter möglich > Teil von Bund Objekt
data class Parameter(val kurztext: String,                      //
                     val langtext: String,                      //
                     val sollIstKennzeichen: String,            //
                     val ermittlungsKennzeichen: String,        //
                     val beareitbar: Boolean,                   //
                     val wert: String,                          //
                     val typ: String,                           //
                     val reihenfolge: Int)                      //
