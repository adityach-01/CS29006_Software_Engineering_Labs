#Imports
import random
from PIL import Image

class CropImage(object):
    '''
        Performs either random cropping or center cropping.
    '''

    def __init__(self, shape, crop_type='center'):
        '''
            Arguments:
            shape: output shape of the crop (h, w)
            crop_type: center crop or random crop. Default: center
        '''
        self.shape = shape
        self.crop_type = crop_type

        # Write your code here

    def __call__(self, image):
        '''
            Arguments:
            image (numpy array or PIL image)

            Returns:
            image (numpy array or PIL image)
        '''
        x,y = image.size
        h = self.shape[0]
        w = self.shape[1]

        if self.crop_type == 'center':
            left = (x-w)/2
            right = (x+w)/2
            top = (y-h)/2
            bottom = (y+h)/2

        elif self.crop_type == 'random':
            top = random.randint(0,y-h)
            bottom = top + h
            left = random.randint(0,x-w)
            right = left + w
           
        image_final = image.crop((left, top, right, bottom))
        return image_final

        # Write your code here



 