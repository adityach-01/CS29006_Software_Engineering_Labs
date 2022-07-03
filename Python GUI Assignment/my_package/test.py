from PIL import Image
import numpy as np
import cv2

image = Image.open('./data/imgs/0.jpg')
image_np = np.array(image)
image_np = np.transpose(image_np,(2,0,1))
# image_np = image_np/255
# cv2 does not care if the values are scaled or not, just make sure that the input array i s(H,W,3)

print(image_np.shape)
image_np = np.transpose(image_np,(1,2,0))
print(image_np.shape)
cv2.imshow("image",image_np)
cv2.waitKey()