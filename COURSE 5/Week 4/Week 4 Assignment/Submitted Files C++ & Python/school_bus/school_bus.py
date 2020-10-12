# python3
from itertools import combinations

INF = 10 ** 9


def read_data():
    n, m = map(int, input().split())
    graph = [[INF] * n for _ in range(n)]
    for _ in range(m):
        u, v, weight = map(int, input().split())
        u -= 1
        v -= 1
        graph[u][v] = graph[v][u] = weight
    return graph


def print_answer(path_weight, path):
    print(path_weight)
    if path_weight == -1:
        return
    print(' '.join(map(str, path)))


def optimal_path(graph):
    n = len(graph)
    C = [[INF for _ in range(n)] for _ in range(1 << n)]
    backtrack = [[(-1, -1) for _ in range(n)] for _ in range(1 << n)]

    C[1][0] = 0
    for size in range(1, n):
        for S in combinations(range(n), size):
            S = (0,) + S
            # print(S)
            k = sum([(1 << i) for i in S])
            for i in S:
                if i != 0:
                    for j in S:
                        if j != i:
                            current = C[k ^ (1 << i)][j] + graph[i][j]
                            if current < C[k][i]:
                                C[k][i] = current
                                backtrack[k][i] = (k ^ (1 << i), j)

    min_cost, index2 = min([(C[(1 << n) - 1][i] + graph[0][i], i) for i in range(n)])
    print(index2)

    if min_cost >= INF:
        return -1, []

    min_path = []
    index1 = (1 << n) - 1
    while -1 != index1:
        min_path.insert(0, index2 + 1)
        index1, index2 = backtrack[index1][index2]

    return min_cost, min_path


if __name__ == '__main__':
    print_answer(*optimal_path(read_data()))

