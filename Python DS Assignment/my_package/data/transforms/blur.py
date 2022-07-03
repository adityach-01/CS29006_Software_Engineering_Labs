import numpy as np
from PIL import ImageFilter
from PIL import Image

class BlurImage(object):
    '''
        Applies Gaussian Blur on the image.
    '''

    def __init__(self, radius):
        '''
            Arguments:
            radius (int): radius to blur
        '''
        self.radius = radius

        # Write your code here
        

    def __call__(self, image):
        '''
            Arguments:
            image (numpy array or PIL Image)

            Returns:
            image (numpy array or PIL Image)
        '''

        im_final = image.filter(ImageFilter.GaussianBlur(radius = self.radius))

        # Returns the image in PIL format
        return im_final
  

# image = Image.open('./data/imgs/0.jpg')
# a = BlurImage(5)
# imagef = a.__call__(image)
# imagef.show()