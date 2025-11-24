import numpy as np
import matplotlib.pyplot as plt

# Generate the data points. 
# return X: array containing one data point per column : shape (2,m) 
def generate_points(m=100, seed=7):
    rng = np.random.RandomState(seed)
    # random poins in boundary [-1,1] x [-1,1]
    X = rng.uniform(-1, 1, size=(2, m))
    return X

# Generate random decision boundary
# return w: weight vector representing decision boundary, of shape (3,1)
def random_boundary(seed=7):
    rng = np.random.RandomState(seed)
    # random points for A and B
    A = rng.uniform(-1, 1, size=2)
    B = rng.uniform(-1, 1, size=2)
    #line trough A and B
    w1 = B[1] - A[1]
    w2 = A[0] - B[0]
    w0 = -(w1*A[0] + w2*A[1])
    w = np.array([w0, w1, w2]).reshape(3,1)
    return w

# Define function that calculates predictions
def predict(w, X_ext):
    scores = np.matmul(w.T, X_ext)
    predictions = np.sign(scores) # -1 oder +1
    predictions = np.reshape(predictions, (1, X_ext.shape[1]))
    return predictions

# Example run with m=10
m = 10
X = generate_points(m)
X_ext = np.vstack((np.ones((1,X.shape[1])), X))
w = random_boundary()
Y = predict(w, X_ext)

# Visualize the data and the decision boundary
fig, ax = plt.subplots()
ax.scatter(X[0, :], X[1, :], marker='o', c=Y, s=25, edgecolor='k')

xp = np.array((-1,1))
yp = -(w[1]/w[2]) * xp - (w[0]/w[2])
plt.axis([-1.1, 1.1, -1.1, 1.1])
plt.plot(xp, yp, "r-") # decision boundary
plt.show()

# Example of a point
index = 3
print("Point #" + str(index) + " is " + str(X[:,index]))
print("Class #" + str(index) + " is " + str(Y[0,index]))

# Define function for weight update
def weight_update(w, x, y, learning_rate=1):
    h = np.sign(np.dot(w.T, x))
    new_w = w + learning_rate * (y - h) * x
    new_w = np.reshape(new_w, (3,1))
    return new_w

# Perzeptron learning algorithm
def run_perceptron(X_ext, Y, alpha=1, max_steps=10000, seed=None):
    rng = np.random.RandomState(seed)
    w_ = np.zeros((3,1))
    steps = 0
    while steps < max_steps:
        preds = predict(w_, X_ext)
        mis_idx = np.where(preds != Y)[1]
        if mis_idx.size == 0:
            break
        j = rng.choice(mis_idx)
        xj_ext = X_ext[:, j].reshape(3,1)
        yj = Y[0, j]
        w_ = weight_update(w_, xj_ext, yj, alpha)
        steps += 1
    return steps

# repeat 1000 times
num_runs = 1000
steps_list = []
for r in range(num_runs):
    steps = run_perceptron(X_ext, Y, alpha=1, seed=r)
    steps_list.append(steps)

avg_steps = np.mean(steps_list)
print(f"average steps over {num_runs} runs (m={m}, alpha=1): {avg_steps:.2f}")

# Experimente mit m=100 und m=1000, alpha=1 und alpha=0.1
for m in [100, 1000]:
    for alpha in [1, 0.1]:
        X = generate_points(m)
        X_ext = np.vstack((np.ones((1,X.shape[1])), X))
        w = random_boundary()
        Y = predict(w, X_ext)
        steps_list = []
        for r in range(100):  #100 and 1000 repetitions for more accurate results
            steps = run_perceptron(X_ext, Y, alpha=alpha, seed=r)
            steps_list.append(steps)
        avg_steps = np.mean(steps_list)
        print(f"average steps (m={m}, alpha={alpha}): {avg_steps:.2f}")
