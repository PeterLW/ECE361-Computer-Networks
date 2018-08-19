
//修改： 给class加上mutator和accessor,member var 用private，在main里面调用member function来改member var的值

class Edge
{
	private：  
	Node destination;  // destination node target
	double cost; // the delay, in ms weight

	public Edge()
	{
	}
	public Edge(Node destination_, double cost_) //constructor to create an instance 
	{ 
		destination = destination_;
		cost = cost_; 
	}
	//accessor
	public int get_destination()  // constructor to create an instance of this class
	{ 
		return this.destination; 
	}
	public int get_cost()
	{ 
		return this.cost; 
	}
	
}

class Node implements Comparable<Node>
{
	private:
	int name;    // Node’s name
	Node previous_node;     // to keep the pat
	double min_distance = Double.POSITIVE_INFINITY; //Minimum weight
	public:
	Edge[] neighbors;  // set of neighbors to this node
	
	public Node()  // constructor to create an instance of this class
	{
	}
	public Node(int name_)  // constructor to create an instance of this class
	{ 
		name = name_; 
	}
	
	//mutator
	public set_name(int name_)  // constructor to create an instance of this class
	{ 
		name = name_; 
	}
	public set_minDistance(int min_distance_)  // constructor to create an instance of this class
	{ 
		min_distance = min_distance_; 
	}
	public set_previous(Node previous_node_)  
	{ 
		previous_node = previous_node_; 
	}

	//accessor
	public int get_name()  // constructor to create an instance of this class
	{ 
		return this.name; 
	}
	public int get_minDistance()
	{ 
		return this.min_distance; 
	}
	public Node get_previous()  
	{ 
		return this.previous_node; 
	}

	public int compareTo(Node other)
	{
		return Double.compare(min_distance, other.min_distance);
	}
}