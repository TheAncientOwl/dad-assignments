# OS: Ubuntu 22.04.1 LTS on Windows 10 x86_64

# HowTo 1 - docker and 2 nodes
  # Requirements:
    2 docker containers
    ssh setup
    ip names: node1, node2

  # let node1 be master
  export OMPI_MCA_btl_vader_single_copy_mechanism=none

  mpicc mpi_add_arrays.c -o mpi_add_arrays.out

  # copy mpi_add_arrays.out in node2 (same path)

  mpirun -np 4 -hostfile add-arrays-nodefile mpi_add_arrays.out

# HowTo 2 - single node
  ./run.sh
