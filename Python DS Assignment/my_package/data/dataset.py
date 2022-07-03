# Imports
import json
from PIL import Image
import numpy as np
# import transforms.crop as crop

class Dataset(object):
    '''
        A class for the dataset that will return data items as per the given index
    '''
    image_annotation_list = []

    def __init__(self, annotation_file, transforms=None):
        '''
            Arguments:
            annotation_file: path to the annotation file
            transforms: list of transforms (class instances)
                        For instance, [<class 'RandomCrop'>, <class 'Rotate'>]
        '''
        self.annotation_file = annotation_file
        self.transforms = transforms

        for line in open(self.annotation_file):
            self.image_annotation_list.append(json.loads(line))
        # Making the list of lines in the annotation file and adding them in image_annotation_list

    def __len__(self):
        '''
            return the number of data points in the dataset
        '''

        return len(self.image_annotation_list)


    # Function to convert image to numpy array after applying the transformation
    def image_To_Numpy(self,path,type_of_image):
        image = Image.open(path)
    
        if(image is None):
            print("Unable to open image")
            return
       
        if(type_of_image =='image' and self.transforms!=None):
            for transformation in self.transforms:
            #   print(transformation)
              image = transformation(image) 

        image_np = np.array(image)

        # The error was happening as the masked images have a different dimension as compared to the RGB images, hence this step should be limited to only RBG image
        if(type_of_image == "image"):
            image_np = np.transpose(image_np,(2,0,1))
        # Making the array (3,H,W)

        return (image_np/255)
        # Scaling the values in the numpy array

    def __getitem__(self, idx):
        
        '''
            return the dataset element for the index: "idx"
            Arguments:
                idx: index of the data element.

            Returns: A dictionary with:
                image: image (in the form of a numpy array) (shape: (3, H, W))
                gt_png_ann: the segmentation annotation image (in the form of a numpy array) (shape: (1, H, W))
                gt_bboxes: N X 5 array where N is the number of bounding boxes, each
                            consisting of [class, x1, y1, x2, y2]
                            x1 and x2 lie between 0 and width of the image,
                            y1 and y2 lie between 0 and height of the image.

            You need to do the following,
            1. Extract the correct annotation using the idx provided.
            2. Read the image, png segmentation and convert it into a numpy array (wont be necessary
                with some libraries). The shape of the arrays would be (3, H, W) and (1, H, W), respectively.
            3. Scale the values in the arrays to be with [0, 1].
            4. Perform the desired transformations on the image.
            5. Return the dictionary of the transformed image and annotations as specified.
        '''
        
        absolute_path=self.annotation_file.rsplit('/',1)[0] +'/'+self.image_annotation_list[idx]['img_fn']
        # Making the absolute path of the image path in the annotation file
        
        # Converting the image to numpy
        image_numpy = self.image_To_Numpy(absolute_path,'image')
      
        path_absolute_png=self.annotation_file.rsplit('/',1)[0] +'/'+self.image_annotation_list[idx]['png_ann_fn']
        # Absolute path of the png image

        image_png_numpy = self.image_To_Numpy(path_absolute_png, 'png')
        # Converitng png image to numpy array

        return_dict={}
        return_dict['image']=image_numpy

        return_dict['gt_png_ann']=image_png_numpy
        return_dict['gt_bboxes']=self.getbbboxes(idx)

        return return_dict
        # Returning the dictionary that way supposed to be returned


    def getbbboxes(self,idx):
        bbox=[]
        for box in self.image_annotation_list[idx]['bboxes']:
            temp=[]
            temp.append(box['category'])
            temp.extend(box['bbox'])
            bbox.append(temp)
        return bbox

# a = Dataset('./data/annotations.jsonl')
# print(a[2]['gt_bboxes'])

        
