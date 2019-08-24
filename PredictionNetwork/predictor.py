from datetime import time
from tkinter import *
import time
import numpy
from keras.layers import Input
from keras.models import Model, Sequential
from keras.layers.core import Dense, Dropout
from keras.layers.normalization import BatchNormalization
from keras.layers.advanced_activations import LeakyReLU
from keras_tqdm import TQDMCallback
from keras.optimizers import Adam
from keras import initializers
from tqdm import tqdm
import os
import random

os.environ["KERAS_BACKEND"] = "tensorflow"

root = Tk()
canvas = Canvas(root, width=500, height=300)
canvas.pack()
tW = canvas.create_text(300, 250, text="Hello123", justify='center', fill='white')
tB = canvas.create_text(200, 250, text="Hello123", justify='center', fill='black')
circleX = 250
circle = canvas.create_oval(circleX-5, 230-5, circleX+5, 230+5, fill='white', width=1)
# state_bubble = 0
array_counter = 0
out = 0
inp = 0


def main():
    global out, inp

    bg, answer = load_data("TrainingData.csv")
    bg_train, bg_test = break_data(bg)
    ans_train, ans_test = break_data(answer)
    model = build_model()
    model.fit(bg_train, ans_train, epochs=130, batch_size=10)
    # model.fit(bg, answer, epochs=130, batch_size=10, callbacks=[TQDMCallback()])
    _, accuracy = model.evaluate(bg_test, ans_test)
    print('Accuracy: %.2f' % (accuracy*100))

    inp = build_prediction_arr()
    out = model.predict(inp)
    # print(numpy.round(out))
    out = numpy.round(out)

    Button(root, text="Quit", command=root.quit).pack()
    root.after(0, place_circle)
    root.mainloop()


def build_prediction_arr():
    pred_arr = numpy.zeros(shape=(20,3))
    for i in range(20):
        r = random.randint(0, 255)
        g = random.randint(0, 255)
        b = random.randint(0, 255)
        pred_arr[i] = [r, g, b]
    return pred_arr


def break_data(data):
    train = data[:750]
    test = data[750:]
    return train, test


def display_answer(rgb, answer):
    global circleX
    if answer == 0:
        canvas.move(circle, 200-circleX, 0)
        circleX = 200
    elif answer == 1:
        canvas.move(circle, 300 - circleX, 0)
        circleX = 300
    canvas.configure(bg="#%02x%02x%02x" % rgb)
    canvas.update()


def place_circle():
    global array_counter, out, inp
    display_answer((int(inp[array_counter, 0]), int(inp[array_counter, 1]), int(inp[array_counter, 2])), int(out[array_counter, 0]))
    array_counter = random.randint(0, 19)
    # print(int(out[array_counter, 0]))
    root.after(1000, place_circle)


def load_data(file_name):
    initial_data = numpy.genfromtxt(file_name, delimiter=',')
    training_input = initial_data[:, 0:3]
    traning_answer = initial_data[:, 3]
    return training_input, traning_answer  # 0 is input, 1 is answer


def build_model():
    model = Sequential()
    model.add(Dense(12, input_dim=3, activation='relu'))
    model.add(Dense(8,activation='relu'))
    model.add(Dense(1, activation='sigmoid'))

    model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
    return model


if __name__ == "__main__":
    main()
