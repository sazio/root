function [u_matrix,v_matrix] = angles2img(r_matrix, epsilon_matrix, chi_matrix,pixel_size,img_size)
%%%IN RADIANS%%%%%%%%%%%%%%%%
% This function transforms points in the angular form that lay in the sphere into image points
% r_matrix, epsilon_matrix, chi_matrix, angular coordinates in the sphere ( NOT SPHERICAL COORDINATES)
% img_size: image size in pixels where the patch was taken from
% pixel_size:size (in mm) of a pixel (if a camera, usual sensor array size is 45mmx34mm) (pixel size = (number of pixels per row)/(horizontal sensor size)) (in our case the images are square, and the pixels as well)
% u_matrix, v_matrix coordinates of the point in the image
[xData,yData] = img2hom([1 img_size(1)], [1 img_size(2)],[1 img_size(1)], [1 img_size(2)],pixel_size);

[x_matrix, y_matrix] = angles2projective(r_matrix, epsilon_matrix, chi_matrix);
[u_matrix,v_matrix] = hom2img(x_matrix, y_matrix,xData, yData,pixel_size);
