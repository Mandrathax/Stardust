/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;

/**
 *
 * @author Paul
 */
public class EntityList {
    Entity hd;
    EntityList tl;
    
    public EntityList(Entity e, EntityList el){
        hd=e;
        tl=el;
        hd.setID(el==null?0:(tl.hd.getID()+1));
    }
    public EntityList(EntityList el){
        hd=el.hd;
        tl=el.tl;
        hd.setID(el.hd.getID());
    }
    
    public static EntityList add(Entity e,EntityList el){
        return new EntityList(e,el);
    }
    
    public static int getLength(EntityList el){
        int ret = 0;
        for(EntityList c=el; c!=null;c=c.tl){
            ret++;
        }
        return ret;
    };
    public static EntityList remove(EntityList el,Entity e){
        EntityList buff=null;;
        for(EntityList c=el; c!=null;c=c.tl){
            if (c.hd.getID()==e.getID()){
                if (buff==null){
                    el=el.tl;
                }else{
                    buff.tl=c.tl;
                } 
            }
            buff=c;
        }
        return el;
    }
    
}
