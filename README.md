# McWBalance
This project is intended as an easy to use GUI mine water balance development platform. Mine water balances are key to
mine planning and tailings management. The balances are specialized in that they must account for void losses within
a deposited tailings mass as well as keep track of all source landcovers to allow for predictive water quality models
to be developed.  Existing tools for this work include Excel and GoldSim. Both have significant advantages and disadvantages
when compared to eachother. This tool aims to combine the visual nature of Goldsim, and the transparency of Excel, with hopefully
a bit more ease of use and reduced learning curve. 

Philosophy:
1) The balance must balance, above all, inputs and outputs must add up, no rounding error
2) The tool must be intuitive and behave similarly to other progams
3) No hidden switches!, logic must be simple and easy to review at a glance
4) Similary Code must be clear and annotated. 
5) Usable output, the user should not have to spend days in Excel formatting the output to make it presentable.
6) Visuals must meet professional standards,
7) FileSize, no unessisary repositories, should be able to email the program, ideal if it fits on a floppy.    

The key requirements of this water balances sofware are as follows:
-  Simple GUI interface, similar to HydroCAD.
-  Manage multiple inputs and outputs form each Element / Node
-  Track solids depositon and void loss
-  Prioritize inputs and outputs over others following simple operating rules
-  Visually graph data within software to check balances and function
-  Output of data to a consistent and well formatted xlsx or xml file. formatting does not require borders but must be setup for
   easy cut and paste to standard deliverable templates
-  Manage various stages of mine life (i.e. pump 1 comes online on day 500, Basin 2 is closed Day 890)

Additional "features" of this program will include:
-  packaged save files
-  ability to export standard figures to .png format
-  ability to handel mutiple climate sets to allow for montecarlo style simulations

Main ToDo before progam will be ready for any meaningful testing
- add Climate data
- complete save and load functionality
- complete runsettings
- complete solve method
- build initial results output (even if just tab delimited ASCII) to start
