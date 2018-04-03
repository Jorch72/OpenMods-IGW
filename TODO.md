# TODO
List of things to complete before version **1.0**

- [ ] Add block documentation
  - [X] Auto Anvil
  - [X] Auto Enchantment Table
  - [X] Bear Trap
  - [X] Big Button
  - [X] Block Breaker
  - [X] Block Placer
  - [X] Builder Guide (IGN: Enhanced Building Guide)
  - [X] Cannon (IGN: Item Cannon)
  - [X] Canvas
  - [X] Canvas - Glass version (IGN: Glass Canvas)
  - [X] Donation Station
  - [X] Drawing Table
  - [X] Elevator
  - [X] Elevator (Rotating)
  - [X] Fan
  - [X] Flag
  - [X] Golden Egg
  - [X] Grave
  - [X] Guide (the normal one)
  - [X] Healer
  - [X] Item Dropper
  - [ ] Ladder
  - [ ] Paint Can
  - [ ] Paint Mixer
  - [ ] Path
  - [ ] Projector
  - [ ] Rope Ladder
  - [ ] Scaffolding
  - [ ] Sky Block
    - [ ] Inverted
	- [ ] Normal
  - [ ] Sponge
  - [ ] Sprinkler
  - [ ] Tank
  - [ ] Target
  - [ ] Trophy
  - [ ] Vacuum Hopper
  - [ ] Village Highlighter
  - [ ] XP Juice (see also Fluids)
  - [ ] XP Bottler
  - [ ] XP Drain
  - [ ] XP Shower
- [ ] Add item documentation
  - [ ] Assistant Base
  - [ ] Beam
  - [ ] Cartographer
  - [ ] Crane Backpack
  - [ ] Crane Controller
  - [ ] Crane Engine
  - [ ] Crane Magnet
  - [ ] Crayon
  - [ ] Cursor
  - [ ] /dev/null
  - [ ] Empty Height Map (the unobtainable one?)
  - [ ] Epic Eraser
  - [ ] Glasses
    - [ ] Admin (Serious)
	- [ ] Crayon
	- [ ] Pencil
	- [ ] Technicolor
  - [ ] Glider Wing
  - [ ] Golden Eye
  - [ ] Hang Glider
  - [ ] Height Map (or is this one?)
  - [ ] Info Book
  - [ ] Line
  - [ ] Luggage
  - [ ] Map Controller
  - [ ] Map Memory
  - [ ] Paintbrush
  - [ ] Pedometer
  - [ ] Pencil
  - [ ] Pointer
  - [ ] Sketching Pencil
  - [ ] Sleeping Bag
  - [ ] Slimalyzer
  - [ ] Sonic Glasses
  - [ ] Sponge (On A Stick)
  - [ ] Squeegee
  - [ ] Stencil
  - [ ] Tasty Clay
  - [ ] Unprepared Stencil
  - [ ] Wrench
  - [ ] XP Bucket
- [ ] Add fluid documentation
  - [ ] XP Juice (see Blocks)
- [ ] Add entity documentation
  - [ ] Cartographer
  - [ ] Luggage
  - [ ] Mini Me
- [ ] Add other bits and pieces
  - [ ] About page
  - [ ] Credits (rewrite)
  - [ ] Enchantments (rewrite)
  - [ ] OB Utilities (rewrite, also move `B` key inside here)
  - [ ] Changelog (for beta testers and higher)
  - [ ] Contacts
- [ ] Provide templates
  - [X] Block
  - [ ] Item
  - [ ] Fluid
  - [ ] Entity
  - [ ] Tab (?)
- [ ] Check if all names match
- [ ] Current history stops at OBv1.6.0: actualize history up to latest version
- [ ] Rework API in order to vaporize the service pattern
- [ ] **FIX**: Some config types not displayed correctly (**utility class**)
  - [ ] Arrays
- [ ] URL splitting on custom missing mod GUI
- [ ] Mismatching mod GUI tweaks
  - [ ] Better gradient (from yellow to red, maybe?)
  - [ ] Switching gradient (from completely color 1 to completely color 2 and vice versa, for beta testers)
- [ ] OpenMods-IGW wiki page

### Service pattern notes
In the end we are still developing a mod for Minecraft, so we want to access its internals without too much of an hassle.
At the same time, we want mod developers to be able to use our API without having to import an entire mod.
Let's join the two worlds together then: we try to abstract some stuff out using interfaces, but referring to our internals if needed.
At the same time, if something in the API needs a deeper entry point, it should be refactored or the method must be filled with ASM.
I lean more on the first option, though.
Services are going to go away, but some bits and pieces (such as `IntegrationHandler`) will be moved to the implementation and probably filled with more implementation related stuff.
And then, well... go for interfaces!
Probably we could also use the Java EE approach and use `@Inject`s or similar: I still need to think about that.

### Javadoc
Not on the current TODO list, sorry.
Too much work already due to API refactor.

### About Projects
Yes, we will move to projects once OpenWiki 1.0 is released and this file is nuked out of orbit.
No, OpenWiki and OpenMods-IGW are the same thing.
No, I still don't know about the name.
Yes, stop asking.
