# -*- coding: utf-8 -*-
"""
Created on Sat Jan 27 11:50:57 2018

@author: Divyanshu
"""

import cv2
cap = cv2.VideoCapture(0)
ret=True
while (ret):
    ret,image_np = cap.read()
  
    if cv2.waitKey(20) & 0xFF == ord('b'): 
# load the test image
        print("Hello\n")
        cv2.imwrite('opencv'+'.jpg', image_np) 
    cv2.imshow('image',cv2.resize(image_np,(640,480)))
    if cv2.waitKey(2) & 0xFF == ord('q'):
        cv2.destroyAllWindows()
        cap.release()
        break
