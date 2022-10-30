function MTF = get_MTF(epsilon,fx,fy)
% gets the approximate MTF function of the human eye at a given
% eccentricity. This function implements the approximation suggested by
% Navarro, Artal and Williams, 1993, Modulation transfer of the human eye as a function of
% retinal eccentricity
% epsilon:eccentricity
% fx,fy: frequency bands (determine the size of the MTF kernel)
A1 = 0.1743;
A2 = 0.0392;
B1 = 0.0362;
B2 = 0.0172;
C1 = 0.215;
C2 = 0.00294;
fr = sqrt(fx.^2+fy.^2);
MTF =  (1-C1+C2*epsilon)*exp(-A1*exp(A2*epsilon)*fr)+(C1-C2*epsilon)*exp(-B1*exp(B2*epsilon)*fr);
