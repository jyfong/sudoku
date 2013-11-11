

class Queue{
    
    int tail;
    int head;
    Vertex[] q = new Vertex[10];
    
    void ENQUEUE(Queue Q, Vertex x){
        Q.q[Q.tail] = x;
        if(Q.tail == Q.q.length)
              Q.tail = 1;
        else  Q.tail = Q.tail + 1;
    }
    
    Vertex DEQUEUE(Queue Q){
        Vertex x = Q.q[Q.head];
        if(Q.head == Q.q.length)
              Q.head = 1;
        else  Q.head = Q.head + 1;
        return x;
    }
}

class Graph{
    
    int[][] adj = {{},{2},{3},{},{1,5,7},{2,6,8},{3},{8},{},{6,8}};
    Vertex[] V = new Vertex[10];
    
    void init(){
        for (int i = 1; i < 10; i++) {
            V[i] = new Vertex(i);
        }
        
        for (int i = 1; i < 10; i++) {
            for (int j = 0; j < adj[i].length; j++) {
                V[i].Adj[j] = V[adj[i][j]];
            }
        }
    }
    
    void initGraph(){
        Vertex u;
        for(int i = 1; i < V.length; i++){
            u = V[i];
            u.color = Vertex.Color.WHITE;
            u.d = Integer.MAX_VALUE;
            u.pi = null;
        }
    }
    
    void BFS(Graph G, Vertex s){
        Vertex u, v;
        s.color = Vertex.Color.GRAY;
        s.d = 0;
        s.pi = null;
        Queue Q = new Queue();
        Q.ENQUEUE(Q,s);
        System.out.printf("s: %s d: %d\n",s.v,s.d);
        while(Q.head != Q.tail){
            u = Q.DEQUEUE(Q);
            for (int i = 0; i <u.Adj.length; i++) {
                if(u.Adj[i]==null) break;
                v = u.Adj[i];
                
                if(v.color == Vertex.Color.WHITE){
                    v.color = Vertex.Color.GRAY;
                    v.d = u.d + 1;
                    v.pi = u;
                    Q.ENQUEUE(Q, v);
                    System.out.printf("v: %s d: %d\n",v.v,v.d);
                }
            }
            u.color = Vertex.Color.BLACK;
        }
    }
    
    int time;
    void DFS(Graph g){
        Vertex u;
        for (int i = 1; i < V.length; i++) {
            u = V[i];
            u.color = Vertex.Color.WHITE;
            u.pi = null;
        }
        time = 0;
        for (int i = 1 ; i < V.length; i++) {
            u = V[i];
            if(u.color == Vertex.Color.WHITE)
                DFSVisit(g,u);
        }
    }
    
    void DFSVisit(Graph g, Vertex u){
        Vertex v;
        time = time + 1;
        u.d = time;
        u.color = Vertex.Color.GRAY;
        System.out.println(u.v);
        for (int i = 0; i < u.Adj.length; i++) {
            if(u.Adj[i]==null) break;
            v = u.Adj[i];
            if(v.color == Vertex.Color.WHITE){
                v.pi = u;
                DFSVisit(g, v);
            }
        }
        u.color = Vertex.Color.BLACK;
        time = time + 1;
        u.f = time;
    }
    
    
    
}

class Vertex{
    enum Color {WHITE, GRAY, BLACK};
    Color color;
    Integer d;
    Vertex pi;
    Integer v;
    Vertex[] Adj = new Vertex[10];
    int f;
    
    Vertex(int n){
        v = n;
    }
}

class Test{
    
    public static void main(String[] args) {
        
        new Test().search();
    }
    
    void search(){
        Graph g = new Graph();
        g.init();
        g.initGraph();
//        g.BFS(g,g.V[1]);
//        for (int i = 1; i < 10; i++) {
//            if(g.V[i].color == Vertex.Color.WHITE){
//                g.BFS(g, g.V[i]);
//            }
//        }
        
        g.DFS(g);
    }
}