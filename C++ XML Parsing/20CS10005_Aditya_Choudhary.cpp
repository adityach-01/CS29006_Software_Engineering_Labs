#include <iostream>
#include <string>
#include "rapidxml.hpp"
#include "rapidxml_utils.hpp"
#include <bits/stdc++.h>
#include <vector>

#define INF 10000;
    
using namespace std;
using namespace rapidxml;

xml_document<> doc;
xml_node<> * root_node = NULL;

class Node{
    public:
        string id;
        long double latitude;
        long double longitude;
        string name;
        int graph_id;
        int no_ways;

        // Non parameterised constructor
        Node(){}

        // Parameterised constructor
        Node(string id, long double lat, long double lon, string n,int gid, int ways){
            this->id = id;
            this->latitude = lat;
            this->longitude = lon;
            this->name = n;
            this->graph_id = gid;
            this->no_ways = ways;
        }
};

struct node_distance{
    long double distance_nodes;  
    string id;    // Id of the node from which the distance is being calculated
};

struct way_data{
    vector<string> node_ids;
};

struct graph_node{
    int g_id;
    long double distance;
    string id;
    struct graph_node *next;
};

struct graph_node *CreateNode(int g_id, long double d, string id){

    struct graph_node *ptr;
    ptr = (struct graph_node *)malloc(sizeof(struct graph_node));
    ptr->distance = d;
    ptr->g_id = g_id;
    ptr->id = id;
    ptr->next = NULL;

    return ptr;
}

struct head{
    struct graph_node *head;
};


void SearchForNode(string input, int num_nodes, Node *ptr){

    int index = 0;
    for (int i = 0; i < num_nodes; i++)
    {   
        if(input.compare("-1") == 0){
            index = 1;
            break;
        }

        string s_id = ptr[i].id;
        string s_name = ptr[i].name;

        if (s_id.find(input) != std::string::npos){
            // The string has been found
            cout<<"<node id = "<<s_id<<"  lat = "<<ptr[i].latitude<<"  lon = "<<ptr[i].longitude<<"  name = "<<ptr[i].name<<">"<<endl;
            index = 1;
        }

        else if(s_name.find(input) != std::string::npos){
            cout<<"<node id = "<<s_id<<"  lat = "<<ptr[i].latitude<<"  lon = "<<ptr[i].longitude<<"  name = "<<ptr[i].name<<">"<<endl;
            index = 1;
        }
    }
    if(index == 0){
        cout<<"No such entry found !!"<<endl;
    }  
}

void swap(int i, int j, struct node_distance *node){
    struct node_distance a = node[i];
    node[i] = node[j];
    node[j] = a;
}

long double getDistance(Node *ptr,int index,int i,int num_nodes){

    // Converting all the latitude and longitude from degress to radians
    if(index == -1 || i == -1){
        return INF;
    }

    long double lat1 = ptr[index].latitude*((M_PI) / 180);
    long double long1 = ptr[index].longitude*((M_PI) / 180);
    long double lat2 = ptr[i].latitude*((M_PI) / 180);
    long double long2 = ptr[i].longitude*((M_PI) / 180);

    cout << setprecision(10) << fixed;

    long double d_long = long2 - long1;
    long double d_lat = lat2 - lat1;
 
    long double distance = pow(sin(d_lat/2), 2) + cos(lat1)*cos(lat2)*
                          pow(sin(d_long / 2), 2);
 
    distance = 2 * asin(sqrt(distance));
 
    // Radius of Earth in Kilometers R = 6371
    long double Radius_earth = 6371;
     
    // Calculate the result, the answer is in kilometers
    distance = distance * Radius_earth;

    return distance;
}

int getIndex(string ide, Node *ptr, int num_nodes){

    // This function returns the index of a node with a particular id
    // returns -1 if the index is not found
    for (int i = 0; i < num_nodes; i++)
    {   
        string s_id = ptr[i].id;

        if (s_id.compare(ide) == 0){
            // The string has been found
            return i;
        }
    }
    return -1;
}

void InsertNode(struct graph_node *a, struct graph_node *b, struct head *c){

    // Inserting b
    if(c[a->g_id].head == NULL){
        c[a->g_id].head = b;
    }
    else{
        struct graph_node *ptr, *prev;
        ptr = c[a->g_id].head;
        while(ptr != NULL){
            prev = ptr;
            ptr = ptr->next;
        }
        prev->next = b;
    }

    //Inserting a
    if(c[b->g_id].head == NULL){
        c[b->g_id].head = a;
    }
    else{
        struct graph_node *ptr, *prev;
        ptr = c[b->g_id].head;
        while(ptr != NULL){
            prev = ptr;
            ptr = ptr->next;
        }
        prev->next = a;
    }
}

int main(){
    cout << "Parsing my map osm file(mapkgp.xml)....." << endl;

    std::ifstream file("mapkgp.osm");
    std::stringstream buffer;
    buffer << file.rdbuf();
    file.close();
    std::string content(buffer.str());

    // Parse the buffer
    doc.parse<0>(&content[0]); 
   
    // Find out the root node
    root_node = doc.first_node("osm");
   
    int num_nodes = 0;
    int num_ways = 0;
    string nodeid;
    long double lat;
    long double lon;

    for (xml_node<> * map_node = root_node->first_node("node"); map_node; map_node = map_node->next_sibling()){
        num_nodes++;
    }

    // Creating an array of graph nodes
    
    Node *ptr = new Node[num_nodes];

    int index = 0;
    // Iterate over the node in the osm file to find number of nodes and storing the data of nodes
    for (xml_node<> * map_node = root_node->first_node("node"); map_node; map_node = map_node->next_sibling())
    {   
        xml_attribute<> *id_attribute = map_node->first_attribute("id");
        nodeid = id_attribute->value();

        xml_node<> *node = map_node;

        int j = 0;
        for (xml_attribute<> *attr = node->first_attribute("lat");
            attr; attr = attr->next_attribute())
        {
            std::cout << std::setprecision(8) << std::fixed;

            if(j == 0) lat = std::stold(attr->value());
            if(j == 1) lon = std::stold(attr->value());
            j++;
        }

        string Name;
        // Checking if the node has a name
        // Try using map_node->Name()
        int alpha = 0;

        for(xml_node<> * tag_node = map_node->first_node("tag"); tag_node; tag_node = tag_node->next_sibling())
        {
            for (xml_attribute<> *attr_tag = tag_node->first_attribute();
            attr_tag; attr_tag = attr_tag->next_attribute()){
                
                string s = attr_tag->name();
                string p = attr_tag->value();
                if(s.compare("k") == 0){
                    if(p.compare("name") == 0){
                        attr_tag = attr_tag->next_attribute();
                        Name = attr_tag->value();
                        alpha++;
                        break;
                    }
                }
            }

            if(alpha == 1){
                alpha = 0;
                break;
            }
        }
        
        // Creating the node to store the information
        Node graph1 = Node(nodeid,lat,lon,Name,index,0);

        // Adding the object in the array of objects
        ptr[index] = graph1;     
        index++; 
    }

    // Iterating all the way elements and storing the data of the ways

    // Defining a vector to store the data of all the way elements

    vector <struct way_data> WayData;

    for (xml_node<> * way_node = root_node->first_node("way"); way_node; way_node = way_node->next_sibling())
    {   

        // Iterating through the children nodes of the way elements to find the nodes that they contain

        struct way_data way1;

        for(xml_node<> * nd_node = way_node->first_node("nd"); nd_node; nd_node = nd_node->next_sibling())
        {
            // Iterating through all the nd elements of a way element and storing the node ids in a vector

            xml_attribute<> *nd_tag = nd_node->first_attribute();
            way1.node_ids.push_back(nd_tag->value());
        }

        // Pushing the way1 way data node in the vector
        WayData.push_back(way1);
        num_ways++;
    }

    cout<<"The number of nodes in the osm file is : "<<num_nodes<<endl;
    cout<<"The number of ways in the osm file is : "<<num_ways<<endl;

    // PARSING OF THE CODE IS DONE
    // NOW WE WILL IMPLEMANT THE SEARCH FEATURE

    cout<<endl;
    string input = "";
    cout<<"Enter the part of node id or node name to search for the node"<<endl;
    cout<<"Enter -1 to exit the process"<<endl;

     // Now we have to return all the nodes which have the the given substring in the name or id
    while(input.compare("-1")){
        cout<<"Enter : ";
        cin>>input;

        // Function for doing the job
        SearchForNode(input, num_nodes, ptr);
    }
    

    // Now we have to find the k nearest nodes to a given node of the graph
    struct node_distance *distanceArray;
    distanceArray = (struct node_distance *)malloc((num_nodes-1)*sizeof(struct node_distance));

    cout<<"Enter the node id and the number of nearest nodes that you want to check"<<endl;
    cout<<"Enter -1 to exit the process"<<endl;

    string node_id = "";
    int k;
    
    while(true){
        cout<<"Enter id of the node : ";
        cin>>node_id;

        if(node_id.compare("-1") == 0){
            break;
        }

        int index = getIndex(node_id,ptr,num_nodes);
        int val = 0;

        if(index == -1){
            cout<<"Enter the correct id of the node"<<endl;
            continue;
        }

        cout<<"Enter k : ";
        cin>>k;
        for (int i = 0; i < num_nodes; i++)
        {   
            if(index == i){
                continue;
            }
            distanceArray[val].distance_nodes = getDistance(ptr,index,i,num_nodes);
            distanceArray[val].id = ptr[i].id;
            val++;
        }

        // Now we will find the k smallest elements in the array and get their id
        // We will use their id and get all the required information about the k nearest nodes

        // Finding the k nearest nodes in the array
        
        for (int i = 0; i < k; i++)
        {   
            cout << setprecision(10) << fixed;
            long double min = distanceArray[i].distance_nodes;
            int cnt = i;
            for (int j = i; j < num_nodes-1; j++)
            {
                if(distanceArray[j].distance_nodes < min){
                    min = distanceArray[j].distance_nodes;
                    cnt = j;
                }
                // The minimum is the one with index cnt
            }

            // Swapping j with i and repeating this step for k times
            // O(nk) algorithm

            string in_id = distanceArray[cnt].id;
            SearchForNode(in_id, num_nodes, ptr);
            cout<<distanceArray[cnt].distance_nodes<<endl;
            swap(i,cnt,distanceArray);   
        } 
    }


    // GRAPH CREATION

    vector<struct graph_node> Nodelist;
    vector<vector<struct graph_node>> AdjacencyList(num_nodes);

    int per = 0;
    int previndex;

    for (int i = 0; i < WayData.size(); i++)
    {
        for (int j = 0; j < WayData[i].node_ids.size()-1; j++)
        {   
            
            string id1 = WayData[i].node_ids[j];
            string id2 = WayData[i].node_ids[j+1];

            // cout<<"A"<<endl;

            int index;
            previndex = getIndex(id1,ptr,num_nodes);
            index = getIndex(id2,ptr,num_nodes);  
            
            /*
            if(j = 0){
                previndex = getIndex(id1,ptr,num_nodes);
                index = getIndex(id2,ptr,num_nodes);  
            }
            else{
                index = getIndex(id2,ptr,num_nodes); 
            }
            */
            // cout<<"A"<<endl;
            
            long double distance = getDistance(ptr,index,previndex,num_nodes);
        
            // cout<<"A"<<endl;

            struct graph_node a,b;
            a.distance = distance;
            a.g_id = previndex;
            a.id = id1;
            b.distance = distance;
            b.g_id = index;
            b.id = id2;

            // cout<<"A"<<endl;
            
            /*
            AdjacencyList[index].push_back(a);
            AdjacencyList[previndex].push_back(b);
            */
           
           per++;
        }
    }
    
    // GRAPH HAS BEEN CREATED

    return 0;
}

