#Imports
import cv2
import matplotlib.pyplot as plt
from PIL import Image
import numpy as np

def plot_visualization(image_numpy,PredictionBoxes=None, PredictionMasks=None, PredictionClass=None, PredictionScore=None): # Write the required arguments

  # The function should plot the predicted segmentation maps and the bounding boxes on the images and save them.
  # Tip: keep the dimensions of the output image less than 800 to avoid RAM crashes.

  # Plotting only the 3 most confident bounding boxes
  # image as (H,W,3)

  image_list = [(1,0,0),(0,1,0),(0,0,1)]
  # image_numpy = image_numpy*255
  # data = Image.fromarray(image_numpy)
  # data = image_numpy
  # image_list.append(data) 
  image_x = np.copy(image_numpy)

  if len(PredictionBoxes) > 3:
    PredictionBoxes = PredictionBoxes[:3]
    PredictionMasks = PredictionMasks[:3]
    PredictionClass = PredictionClass[:3]
    PredictionScore = PredictionScore[:3]

  # Adding the bounding boxes to the image using opencv
  for i in range(len(PredictionBoxes)):
    x1 = (int)(PredictionBoxes[i][0][0])
    y1 = (int)(PredictionBoxes[i][0][1])
    x2 = (int)(PredictionBoxes[i][1][0])
    y2 = (int)(PredictionBoxes[i][1][1])
    image_x = cv2.rectangle(image_x, (x1, y1), (x2, y2),image_list[i], 2)
    cv2.putText(image_x,PredictionClass[i] + "  "+ str(PredictionScore[i]),(x1, y1-10),cv2.FONT_HERSHEY_SIMPLEX,0.6,(1,1,0),2)

    # cv2.rectangle needs input as a nnumpy array of the form (H,W,3) to work
    # cv2.imshow can also rea numpy arrays as images of the form (H,W,3)

  for i in range(len(PredictionMasks)):
    PredictionMasks[i] = np.transpose(PredictionMasks[i],(1,2,0))
    # Changing them to the official array format
  a = np.zeros((PredictionMasks[0].shape[0],PredictionMasks[0].shape[1],1))
  # Shape of the segmentation mask array is (H,W,1)
  # Take care in the future

  for i in range(len(PredictionMasks)):
    a += PredictionMasks[i]
    # Contains the grayscale value of the prediction mask image

  final = np.zeros((PredictionMasks[0].shape[0],PredictionMasks[0].shape[1],3))

  for i in range(PredictionMasks[0].shape[0]):
    for j in range(PredictionMasks[0].shape[1]):
      if a[i][j] >= 0.6:
        final[i][j][0] = 1
        final[i][j][1] = 0
        final[i][j][2] = 0
      
      elif a[i][j] <= 0.2:
        final[i][j][0] = image_numpy[i][j][0]
        final[i][j][1] = image_numpy[i][j][1]
        final[i][j][2] = image_numpy[i][j][2]
      
      else :
        final[i][j][0] = a[i][j]
        final[i][j][1] = a[i][j]
        final[i][j][2] = a[i][j]


  return image_x, final



# image = Image.open('./data/imgs/0.jpg')
# image_np = np.array(image)
# # image_np = np.transpose(image_np,(2,0,1))
# # image_np = image_np/255

# im = plot_visualization(image,[[(100,200),(200,100)],[(10,90),(90,10)]])
# cv2.imshow("image",im)
# cv2.waitKey()


  
