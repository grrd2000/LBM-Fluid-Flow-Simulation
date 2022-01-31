# Fluid Flow Simulation
## Introduction
College project simulating fluid flow using Lattice Boltzmann methods (LBM) and cellular automata.

## Step by step
In order to run the simulation, the following operations are performed continuously one after the other:
* Calculations of macroscopic values
* Equilibrium distribution calculations
* Collision operation
* Streaming operation
* Boundary conditions

After implementing algorithmic part of the simulation, initial conditions has been added. Both the left and right walls have a linearly varying horizontal velocity. The vertical velocity vector is zero and the density over the entire calculation area is equal to one.

### The final result
<p align="center">
    <img src="./output/fluid_flow_gif_02.gif">
</p>

## Sources and inspirations
* https://en.wikipedia.org/wiki/Lattice_Boltzmann_methods
* https://en.wikipedia.org/wiki/Cellular_automaton