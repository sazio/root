function U = inv_rot_plan_form(X, T)
%invert the rotation
%X are the coordinates (intrinsic)
% T are the extra parameters (r,epsilon,chi) (angular positions)
x1_grid = X(:,1);
y1_grid = X(:,2);

r = T.tdata(1);
epsilon = T.tdata(2);
chi = T.tdata(3);
[xs, ys, zs] = angles2sphere(r, epsilon, chi);
[x_matrix, y_matrix] = int2hom(x1_grid, y1_grid);
z_matrix = -2*r* ones(size(y_matrix));


angle = acos(-(zs+r)/(sqrt(xs^2+ys^2+(zs+r)^2)));
u = -ys;
v= xs;
% if angle >=pi/2
%     angle = acos((zs+r)/(sqrt(xs^2+ys^2+(zs+r)^2)));
%     u = ys;
%     v = -xs;
% end

n = norm([u,v]);
u = u/n;
v = v/n;
%tr = -2*r-z_proj_matrix(1,1);

if isnan(u) || isnan(v)
    m = eye(4);
   % m(3,4) = tr; 
else
    m = [(cos(angle)+u^2*(1-cos(angle))),    u*v*(1-cos(angle)),             v*sin(angle),     0;
         u*v*(1-cos(angle)),               (cos(angle)+v^2*(1-cos(angle))),  -u*sin(angle),    0;
         -v* sin(angle),                   u*sin(angle),                    cos(angle)       0;
        0,                                  0,                              0,                1;
         ];
end
%  t = [xs,ys,-2*r, 0]'-m*[xs, ys, zs, 1]';
%  m(:,4) = t;
 t = [xs,ys,zs, 1]'-m'*[xs, ys, -2*r, 1]';
 m(4,:) = t;




h_coor = [x_matrix(:), y_matrix(:), z_matrix(:), ones(size(x_matrix(:)))]';
x = m'*h_coor;

x_rot = x(1,:)';
y_rot = x(2,:)';
z_rot = x(3,:)';


[x1_rot, y1_rot] = hom2int(x_rot, y_rot); 

U = [x1_rot, y1_rot, z_rot];


