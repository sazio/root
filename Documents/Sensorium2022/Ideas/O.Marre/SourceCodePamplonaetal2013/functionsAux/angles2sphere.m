function [xs_matrix, ys_matrix, zs_matrix] = angles2sphere(r_matrix, epsilon_matrix, chi_matrix)
% transform angular coordinates in the sphere to usual cartesian coordinates in the sphere
% r_matrix, epsilon_matrix, chi_matrix, angular coordinates in the sphere ( NOT SPHERICAL COORDINATES)
% xs_matrix, ys_matrix, zs_matrix cartesian coordinates
zs_matrix = -2*r_matrix.*cos(epsilon_matrix).^2;
xs_matrix = -zs_matrix.*tan(epsilon_matrix).*cos(chi_matrix);
ys_matrix = xs_matrix.*tan(chi_matrix);
%[xs_matrix,ys_matrix,zs_matrix] = sph2cart(chi_matrix,2*epsilon_matrix,r_matrix);
%xs_matrix = r_matrix* sin(2*epsilon_matrix) * cos(chi_matrix);
%ys_matrix = r_matrix * sin(2*epsilon_matrix)*cos(chi_matrix);
%zs_matrix = r_matrix * cos(chi_matrix)
%zs_matrix = -2*r_matrix+zs_matrix;
