Custom methods:

- unregisterMusician() in Conductor class: Created to have a 50% chance of removing the musician from the band.

- getInstrumentID() in Conductor class: Created to compare the music score and musician Instrument ID in the play composition method.

- registerToConductor() in EcsBandAid class: Created to register the musicians to the conductor only once.

---

EXTENSION:

---

This is the extension that I implemented:

"Extend the system to play music with arbitrary length. At the moment, all notes are played at the same length".

The simulator for the extension can be run in the terminal though the following format:

"java EcsBandAid musicians.txt compositions.txt 2" as this is the format the specification asked for.

The compositions.txt should be in this format:

Name: Simple
Tempo: Moderato
Length: 7
Piano, loud, {(G4,1), (G4,1), (G4,2), (D4,1), (E4,1), (E4,1), (D4,2)}
Violin, soft, {(G4,1), (G4,1), (G4,2), (D4,1), (E4,1), (E4,1), (D4,2)}
Violin, loud, {(G4,1), (G4,1), (G4,2), (D4,1), (E4,1), (E4,1), (D4,2)}

Whereby for each element, for example (G4,2), G4 is the note and 2 is the length of the note.

---

I extended EcsBandAid in the readCompositionFile() to separate the notes from the lengths into their respective arrays.

Then I created a method called storeNoteLengthArray() in MusicSheet to retrieve the array of lengths from EcsBandAid.

Afterwards, I created a method called getNoteLengthArray() in MusicSheet to use in the Conductor class.

In the Conductor class, I extended the playComposition() method to play the next note, using a loop to play the length of the note.

Subsequently, depending on the number paired with the note, the duration of the note will change as well.

---

Kind regards,

Patrick







