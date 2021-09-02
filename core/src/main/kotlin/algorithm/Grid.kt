package algorithm

import kotlin.math.abs

data class Position(val line: Int, val col: Int)

class Node(val pos: Position): HeapElement<Node> {
    override var heapIdx: Int = 0
    var parent: Node? = null
    var hCost = 0
    var gCost = 0
    val fCost: Int
        get() {
            return gCost + hCost
        }
    var isTraversable = true
    var isClosed = false
    var type: String = "N"

    override fun toString(): String = type// fCost.toString()


    override fun compareTo(other: Node): Int = if (other.fCost - fCost == 0) other.hCost - hCost else other.fCost - fCost

}

class Grid(
    private val sizeX: Int,
    private val sizeY: Int,
) {
    private val grid: List<List<Node>> = List(sizeY){ i ->
        List(sizeX) {j ->
            Node(Position(i, j))
        }
    }

    fun createWall(start: Position, end: Position){
        val range1 = if (start.line < end.line) start.line..end.line else end.line..start.line
        val range2 = if (start.col < end.col) start.col..end.col else end.col..start.col
        for (i in range1){
            for (j in range2){
                val node: Node = getNodeInGrid(Position(i, j))
                node.isTraversable = false
                node.type = "B"
            }
        }
    }

    private fun getNodeInGrid(point: Position): Node{
        return grid[point.line][point.col]
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

    private fun calculateHCost(node: Node, end: Position): Int {
        val distX: Int = abs(end.line - node.pos.line)
        val distY: Int = abs(end.col - node.pos.col)
        return if (distX > distY) distX*10 + distY*4 else distX*4 + distY*10
    }

    private fun forEachNeighbor(node: Node, func: (Node, Int, Int) -> Unit){
        for (i in -1..1){
            for (j in -1..1){
                if (i == 0 && j == 0) continue

                if( i + node.pos.line in 0 until sizeY &&
                    j + node.pos.col in 0 until  sizeX) {
                    func(grid[i + node.pos.line][j + node.pos.col], i, j)
                }
            }
        }
    }

    fun shortestPath(start: Position, end: Position){
        val endNode: Node = getNodeInGrid(end)

//        val open = mutableListOf(getNodeInGrid(start))
        val open = Heap<Node>(sizeX*sizeY)
        open.add(getNodeInGrid(start))

        while (true) {
            val current = open.pop()
//            val current: Node = getBestNode(open)
//            open.remove(current)
            current.isClosed = true

            if (current == endNode  ){
                printPath(start, end)
                return
            }

            forEachNeighbor(current) { node, i, j ->
                if (!node.isTraversable || node.isClosed) return@forEachNeighbor

                val prevGCost = node.gCost
                val newGCost: Int = current.gCost + if (j == 0 || i == 0) 10 else 14

                if (newGCost < node.gCost || node.gCost == 0){
                    node.gCost = newGCost
                    node.parent = current
                    if (prevGCost == 0){
                        node.hCost = calculateHCost(node, end)
                        open.add(node)
                    } else {
                        open.updateItem(node)
                    }

                }

            }
        }
    }

    private fun printPath(start: Position, end: Position) {
        var node: Node? = getNodeInGrid(end)
        while (node != null){
            node.type = "."
            node = node.parent
        }
        getNodeInGrid(end).type = "E"
        getNodeInGrid(start).type = "S"
        printGrid()
    }

    fun printGrid(){
        grid.forEach { row ->
            println(row.toString())
        }
    }
}

fun main(){
    val g = Grid(10, 10)
    g.createWall(Position(2, 0), Position(2, 5))
    g.createWall(Position(7, 1), Position(7, 9))
    g.printGrid()
    println("--------------------------")
    println("FINAL PATH")
    g.shortestPath(Position(9, 8), Position(0, 1))

}