#Imports
from my_package.model import InstanceSegmentationModel
from my_package.data import Dataset
from my_package.analysis import plot_visualization
from my_package.data.transforms import FlipImage, RescaleImage, BlurImage, CropImage, RotateImage
import numpy as np
from PIL import Image
import cv2
import os

def experiment(annotation_file, segmentor, transforms, outputs):
    '''
        Function to perform the desired experiments

        Arguments:
        annotation_file: Path to annotation file
        segmentor: The image segmentor
        transforms: List of transformation classes
        outputs: path of the output folder to store the images
    '''

    #Create the instance of the dataset.
    dataset = Dataset(annotation_file,transforms)

    #Iterate over all data items.
    image = []
    for i in range(len(dataset)):
        image.append(dataset[i]['image'])
        # List of numpy arrays is image

    #Get the predictions from the segmentor.
    #Draw the segmentation maps on the image and save them.
    
    for i in range(len(image)-1):
        # image_np = np.transpose(image[i],(2,0,1))

        PredictionBoxes, PredictionMasks, PredictionClass, PredictionScore = segmentor(image[i])

        image[i] = np.transpose(image[i],(1,2,0))  # Restoring the shape of the image

        img_boxes,img_masks = plot_visualization(image[i],PredictionBoxes, PredictionMasks, PredictionClass, PredictionScore)

        # cv2.imshow("image.jpg",img_boxes)
        # cv2.waitKey()

        owd =  os.getcwd()
        os.chdir(outputs)
        
        # directory = './data/pngs'
        filename1 = str(i) + 'bb.png'
        filename2 = str(i) + 'mask.png'

        cv2.imwrite(filename1, img_boxes)
        cv2.imwrite(filename2, img_masks)
        os.chdir(owd)
    

    #Do the required analysis experiments.
    
def main():
    segmentor = InstanceSegmentationModel()
    experiment('./data/annotations.jsonl', segmentor, None,'./data/pngs') # Sample arguments to call experiment()


if __name__ == '__main__':
    main()
