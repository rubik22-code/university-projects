#include <stdlib.h>

typedef struct item
{
    int code;
    int quantity;
};

// Allocates space for an array of n item structures

struct item *space (int n)
{
    return malloc(n * sizeof(struct item));
}

// Stores the structure item str at position pos in the array of structures arr

int store (int pos, struct item *arr, struct item str)
{
    if (arr == NULL || pos < 0) {
        return -1;
    }
    else {
        arr[pos] = str;
        return 0;
    }
}

// Returns average value of member quantity across m item structures in the array arr.

double average (struct item *arr, int m)
{
    if (arr == NULL || m <= 0) {
        return -1;
    }

    double total, average;

    for (int x = 0; x < m; x++) {
        total = total + arr[x].quantity;
    }

    average = total / m;
    return average;
}

