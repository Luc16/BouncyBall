package algorithm

import kotlin.math.abs

private class Node(val pos: List<Int>) {
    var parent: Node? = null
    var hCost: Int = 0
    var gCost: Int = 0
    val fCost: Int
        get() {
            return gCost + hCost
        }
    var isTraversable: Boolean = true
    var isClosed: Boolean = false
    var type: String = "N"

    override fun toString(): String {
        return type
    }

}

class Grid(
    private val sizeX: Int, private val sizeY: Int,
    notTraversable: List<List<Int>> = listOf()
) {
    private val grid: Array<Array<Node>> = Array(sizeY){ i ->
        Array(sizeX) {j ->
            Node(listOf(i, j))
        }
    }
    init {
        notTraversable.forEach { point ->
            val node = getNodeInGrid(point)
            node.isTraversable = false
            node.type = "B"
        }
    }

    fun createWall(start: List<Int>, end: List<Int>){
        val range1: IntRange = if (start[0] < end[0]) start[0]..end[0] else end[0]..start[0]
        val range2: IntRange = if (start[1] < end[1]) start[1]..end[1] else end[1]..start[1]
        for (i in range1){
            for (j in range2){
                val node: Node = getNodeInGrid(listOf(i, j))
                node.isTraversable = false
                node.type = "B"
            }
        }
    }

    private fun getNodeInGrid(point: List<Int>): Node{
        return grid[point[0]][point[1]]
    }

    private fun getBestNode(nodes: List<Node>): Node{
        var bestNode = nodes[0]
        for (i in 1 until nodes.size){
            if ((nodes[i].fCost < bestNode.fCost) ||
                ((nodes[i].fCost == bestNode.fCost) &&
                        (nodes[i].hCost < bestNode.hCost))) bestNode = nodes[i]
        }
        return bestNode
    }

    private fun calculateHCost(node: Node, end: List<Int>): Int {
        val distX: Int = abs(end[0] - node.pos[0])
        val distY: Int = abs(end[1] - node.pos[1])
        return if (distX > distY) distX*10 + distY*4 else distX*4 + distY*10
    }

    private fun forEachNeighbor(node: Node, func: (Node, Int, Int) -> Unit){
        for (i in -1..1){
            for (j in -1..1){
                if (i == 0 && j == 0) continue

                if( i + node.pos[0] in 0 until sizeY &&
                    j + node.pos[1] in 0 until  sizeX) {
                    func(grid[i + node.pos[0]][j + node.pos[1]], i, j)
                }
            }
        }
    }

    fun shortestPath(start: List<Int>, end: List<Int>){
        val endNode: Node = getNodeInGrid(end)
        getNodeInGrid(end).type = "E"
        getNodeInGrid(start).type = "S"

        val open = mutableListOf(getNodeInGrid(start))

        while (true) {
            val current: Node = getBestNode(open)
            open.remove(current)
            current.isClosed = true

            if (current == endNode  ){
                printPath(end)
                return
            }

            forEachNeighbor(current) { node, i, j ->
                if (!node.isTraversable || node.isClosed) return@forEachNeighbor

                val newGCost: Int = current.gCost + if (j == 0 || i == 0) 10 else 14

                if (newGCost < node.gCost || node.gCost == 0){
                    node.parent = current
                    if (node.gCost == 0){
                        open.add(node)
                        node.hCost = calculateHCost(node, end)
                    }
                    node.gCost = newGCost
                }

            }
        }
    }

    private fun printPath(end: List<Int>) {
        var node: Node = getNodeInGrid(end).parent!!
        while (node.parent != null) {
            node.type = "."
            node = node.parent!!
        }
        printGrid()
    }

    fun printGrid(){
        grid.forEach { row ->
            println(row.contentToString())
        }
    }
}