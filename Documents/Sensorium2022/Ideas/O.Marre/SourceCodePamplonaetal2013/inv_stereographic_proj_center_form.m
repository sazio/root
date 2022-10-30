function U = inv_stereographic_proj_center_form(X, T)
%invert the stereographic projection
%X are the coordinates (intrinsic)
% T are the extra parameters (r,epsilon,chi) (angular positions)
x1_grid = X(:,1);
y1_grid = X(:,2);
z1_grid = X(:,3);
r = T.tdata(1);
epsilon = T.tdata(2);
chi = T.tdata(3);

[x_matrix, y_matrix] = int2hom(x1_grid, y1_grid);
z_matrix = z1_grid;


[xs, ys, zs] = angles2sphere(r, epsilon, chi);
%mu = 4 *r^2./(x_proj_matrix.^2 + y_proj_matrix.^2+z_proj_matrix.^2+2*r*z_proj_matrix+4*r^2);
NP = [-xs, -ys, -zs-2*r];
mu = 4*r^2./(x_matrix.^2 + y_matrix.^2+z_matrix.^2+2*r*(2*r+z_matrix));
x_sphere = NP(1) + mu.*(x_matrix-NP(1));
y_sphere = NP(2) + mu.*(y_matrix-NP(2));
z_sphere = NP(3) + mu.*(z_matrix-NP(3));
[x1_sphere, y1_sphere] = hom2int(x_sphere, y_sphere);
z1_sphere = z_sphere;
U = [x1_sphere, y1_sphere,z1_sphere];
